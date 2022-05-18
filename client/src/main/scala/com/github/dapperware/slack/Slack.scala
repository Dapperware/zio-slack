package com.github.dapperware.slack

import io.circe.{ Decoder, Json }
import sttp.client3.{ Identity, RequestT }
import sttp.client3.asynchttpclient.zio.SttpClient
import zio.duration.Duration
import zio.{ Has, UIO, URIO, ZIO }

/**
 * The root trait for the Slack API.
 */
trait Slack {

  def apiCall[A](request: Request[A, AccessToken]): URIO[Has[AccessToken], SlackResponse[A]]
  def clientApiCall[A](request: Request[A, ClientSecret]): URIO[Has[ClientSecret], SlackResponse[A]]

  def unauthenticatedApiCall[A](request: Request[A, Unit]): UIO[SlackResponse[A]]

}

object Slack
    extends Apps
    with Auth
    with Bots
    with Calls
    with Chats
    with Conversations
    with Dialogs
    with Dnd
    with Emojis
    with Files
    with OAuth
    with Pins
    with Profiles
    with Reactions
    with Reminders
    with RemoteFiles
    with Search
    with Stars
    with Teams
    with UserGroups
    with Users
    with Views {

  def apiCall[A](
    request: Request[A, AccessToken]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[A]] =
    ZIO.service[Slack].flatMap(_.apiCall(request))

  def clientApiCall[A](
    request: Request[A, ClientSecret]
  ): URIO[Has[Slack] with Has[ClientSecret], SlackResponse[A]] =
    ZIO.service[Slack].flatMap(_.clientApiCall(request))

  def unauthenticatedApiCall[A](
    request: Request[A, Unit]
  ): URIO[Has[Slack], SlackResponse[A]] =
    ZIO.service[Slack].flatMap(_.unauthenticatedApiCall(request))

  def request(name: String): Request[Unit, AccessToken] = Request.make(name)

  def request[A: Decoder](name: String, args: (String, SlackParamMagnet)*): Request[A, AccessToken] =
    Request.make(name).formBody(args: _*).as[A]

}

class HttpSlack private (baseUrl: String, client: SttpClient.Service) extends Slack {

  private def makeCall[A](request: RequestT[Identity, SlackResponse[A], Any]) =
    client
      .send(request)
      .mapBoth(SlackError.fromThrowable, _.body)
      .merge

  def apiCall[A](request: Request[A, AccessToken]): URIO[Has[AccessToken], SlackResponse[A]] =
    ZIO.serviceWith[AccessToken](token => makeCall(request.toRequest(baseUrl).auth.bearer(token.token)))

  override def unauthenticatedApiCall[A](request: Request[A, Unit]): UIO[SlackResponse[A]] =
    makeCall(request.toRequest(baseUrl))

  override def clientApiCall[A](request: Request[A, ClientSecret]): URIO[Has[ClientSecret], SlackResponse[A]] =
    ZIO.serviceWith[ClientSecret](secret =>
      makeCall(request.toRequest(baseUrl).auth.basic(secret.clientId, secret.clientSecret))
    )
}

object HttpSlack {
  final val SlackBaseUrl = "https://slack.com/api/"

  def make: ZIO[Has[SttpClient.Service], Nothing, Slack] = make(SlackBaseUrl)

  def make(url: String): ZIO[Has[SttpClient.Service], Nothing, Slack] =
    ZIO.service[SttpClient.Service].map(new HttpSlack(url, _))
}

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
  def toEitherWith[B](f: (A, List[String]) => B): Either[SlackError, B]
  def map[B](f: A => B): SlackResponse[B]
}
sealed abstract class SlackError extends SlackResponse[Nothing] {
  val isOk: Boolean                                                           = false
  def toEitherWith[B](f: (Nothing, List[String]) => B): Either[SlackError, B] = Left(this)
  def map[B](f: Nothing => B): SlackResponse[Nothing]                         = this
}

object SlackError {
  def fromThrowable(throwable: Throwable): SlackError = CommunicationError(throwable.getMessage, Some(throwable))

  /**
   * Indicates an error occurred from the slack API these messages have a set structure and will
   * always have
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

object SlackResponse {

  def decodeWith[A](f: Json => Either[io.circe.Error, A]): Decoder[SlackResponse[A]] = Decoder.instance { hcursor =>
    hcursor.downField("ok").as[Boolean].flatMap {
      if (_) f(hcursor.value)
      else hcursor.downField("error").as[String].map(SlackError.ApiError(_))
    }
  }

  implicit def decoder[A: Decoder]: Decoder[SlackResponse[A]] = Decoder.instance { hcursor =>
    hcursor
      .downField("ok")
      .as[Boolean]
      .flatMap(
        if (_) hcursor.as[Ok[A]]
        else hcursor.downField("error").as[String].map(SlackError.ApiError(_))
      )
  }

  /**
   * A successful response which may optionally contain a comma delimited list of warnings
   * @param body The response body
   * @param warning The optional warning
   */
  case class Ok[+A](body: A, warning: List[String]) extends SlackResponse[A] {
    val isOk                                                                       = true
    override def toEitherWith[B](f: (A, List[String]) => B): Either[SlackError, B] = Right(f(body, warning))

    override def map[B](f: A => B): SlackResponse[B] = Ok(f(body), warning)
  }

  object Ok {
    implicit def decoder[A: Decoder]: Decoder[Ok[A]] =
      Decoder.forProduct2("body", "warning")(Ok.apply[A])
  }

}
