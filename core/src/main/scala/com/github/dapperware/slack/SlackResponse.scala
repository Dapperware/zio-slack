package com.github.dapperware.slack

import com.github.dapperware.slack.SlackResponse.Ok
import io.circe.{ Decoder, Json }
import zio.Zippable
import zio.Duration

/**
 * A slack response as detailed by the slack api documentation.
 *
 * @see `https://api.slack.com/web#slack-web-api__evaluating-responses`
 */
sealed trait SlackResponse[+A] {

  /**
   * Whether or not the request was successful
   */
  def isOk: Boolean

  /**
   * Converts the response into an either by unpacking the value into the right-hand channel and throwing away any warnings
   */
  def toEither: Either[SlackError, A] = toEitherWith((a, _) => a)

  /**
   * More powerful version of `toEither` which allows the caller to handle the warnings that may be present in the response
   */
  def toEitherWith[B](f: (A, List[String]) => B): Either[SlackError, B]

  /**
   * Maps over over the body of the response
   */
  def map[B](f: A => B): SlackResponse[B]

  /**
   * Constructs a slack response that will contain the results of *both* responses if both are successful
   */
  def both[B](that: SlackResponse[B])(implicit zippable: Zippable[A, B]): SlackResponse[zippable.Out]
}

object SlackResponse extends SlackResponseLowPrio0 {

  def ok[A](value: A, warnings: List[String] = Nil): SlackResponse[A] =
    SlackResponse.Ok(value, warnings)

  def decodeWith[A](f: Json => Either[io.circe.DecodingFailure, A]): Decoder[SlackResponse[A]] = Decoder.instance {
    hcursor =>
      hcursor.downField("ok").as[Boolean].flatMap {
        if (_) Ok.decoder(d => f(d.value))(hcursor)
        else hcursor.downField("error").as[String].map(SlackError.ApiError(_))
      }
  }

  implicit val unitDecoder: Decoder[SlackResponse[Unit]] = decodeWith(_ => Right(()))

  /**
   * A successful response which may optionally contain a comma delimited list of warnings
   * @param body The response body
   * @param warning The optional warning
   */
  case class Ok[+A](body: A, warning: List[String]) extends SlackResponse[A] {
    val isOk                                                                       = true
    override def toEitherWith[B](f: (A, List[String]) => B): Either[SlackError, B] = Right(f(body, warning))

    override def map[B](f: A => B): SlackResponse[B] = Ok(f(body), warning)

    override def both[B](that: SlackResponse[B])(implicit zippable: Zippable[A, B]): SlackResponse[zippable.Out] =
      that match {
        case Ok(b, w)          => Ok(zippable.zip(body, b), warning ++ w)
        case error: SlackError => error
      }
  }

  object Ok {
    implicit def decoder[A: Decoder]: Decoder[Ok[A]] = Decoder.instance { hcursor =>
      for {
        body    <- hcursor.as[A]
        warning <- hcursor.getOrElse[List[String]]("warning")(Nil)
      } yield Ok(body, warning)
    }
  }

}

sealed abstract class SlackError extends SlackResponse[Nothing] {
  val isOk: Boolean                                                                                         = false
  def toEitherWith[B](f: (Nothing, List[String]) => B): Either[SlackError, B]                               = Left(this)
  def map[B](f: Nothing => B): SlackResponse[Nothing]                                                       = this
  def both[B](that: SlackResponse[B])(implicit zippable: Zippable[Nothing, B]): SlackResponse[zippable.Out] = this
}

object SlackError {
  def fromThrowable(throwable: Throwable): SlackError = CommunicationError(throwable.getMessage, Some(throwable))

  /**
   * Indicates an error occurred from the slack API these messages have a set structure and will
   * always have an error message
   */
  case class ApiError(error: String, needed: Option[String] = None, provided: Option[String] = None) extends SlackError

  /**
   * Errors that could not be parsed, these usually indicate some other type of network error
   * that prevented processing
   * @param status The network status code
   * @param body The unparsed body of the error response
   */
  case class HttpError(status: Int, body: String) extends SlackError

  /**
   * A special case error which indicates that you have exceeded your rate limit.
   * @param retryAfter The period of time to wait before retrying the request
   */
  case class RatelimitError(retryAfter: Duration) extends SlackError

  /**
   * Indicates a network error occurred usually before the request could even be made (i.e. no internet connection)
   */
  case class CommunicationError(message: String, innerThrowable: Option[Throwable]) extends SlackError
}

private[slack] trait SlackResponseLowPrio0 {

  implicit def decoder[A: Decoder]: Decoder[SlackResponse[A]] = Decoder.instance { hcursor =>
    hcursor
      .downField("ok")
      .as[Boolean]
      .flatMap(
        if (_) hcursor.as[Ok[A]](Ok.decoder[A])
        else hcursor.downField("error").as[String].map(SlackError.ApiError(_))
      )
  }
}
