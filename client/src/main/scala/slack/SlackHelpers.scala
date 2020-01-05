package slack

import cats.Show
import io.circe.{ACursor, Decoder, DecodingFailure, Json}
import zio.{IO, ZIO}

trait SlackHelpers {

  private def ok(cursor: ACursor): Either[DecodingFailure, Boolean] =
    cursor.downField("ok").as[Boolean]

  private def extractBody[A: Decoder](json: Json, key: Option[String]): IO[SlackError, A] = IO.fromEither {
    val c = json.hcursor
    for {
      ok <- ok(c)
      body <- if (ok) key.fold(c.as[A])(c.downField(_).as[A]) else c.as[SlackException.ResponseError].flatMap(Left(_))
    } yield body
  }

  def isOk(json: Json): IO[DecodingFailure, Boolean] =
    ZIO.fromEither(ok(json.hcursor))

  def as[A: Decoder](key: String)(json: Json): IO[SlackError, A] =
    extractBody(json, Some(key))

  def as[A: Decoder](json: Json): IO[SlackError, A] =
    extractBody(json, None)

  implicit val show: Show[String] = cats.instances.string.catsStdShowForString

}

object SlackHelpers extends SlackHelpers
