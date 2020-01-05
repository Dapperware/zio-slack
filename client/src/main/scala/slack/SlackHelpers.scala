package slack

import cats.Show
import io.circe.{Decoder, DecodingFailure, Json}
import zio.{IO, ZIO}

trait SlackHelpers {

  def isOk(json: Json): IO[DecodingFailure, Boolean] =
    as[Boolean]("ok")(json)

  def as[A: Decoder](key: String)(json: Json): IO[DecodingFailure, A] =
    ZIO.fromEither(json.hcursor.downField(key).as[A])

  def as[A: Decoder](json: Json): IO[DecodingFailure, A] =
    ZIO.fromEither(json.as[A])

  implicit val show: Show[String] = cats.instances.string.catsStdShowForString

}

object SlackHelpers extends SlackHelpers
