package com.github.dapperware.slack

import com.github.dapperware.slack.Request.{ extractBodyAt, respondWith }
import com.github.dapperware.slack.client.RequestEntity
import io.circe
import io.circe.syntax.EncoderOps
import io.circe.{ Decoder, Encoder, Json }
import sttp.client3.circe._
import sttp.client3.json.RichResponseAs
import sttp.client3.{
  asString,
  basicRequest,
  BasicRequestBody,
  DeserializationException,
  HttpError,
  Identity,
  IsOption,
  RequestT,
  ResponseAs,
  ResponseException,
  UriContext
}
import sttp.model.{ Part, StatusCode }
import zio.{ durationLong, Duration }

case class Request[+T, Auth](
  method: MethodName,
  body: SlackBody,
  responseAs: Json => Either[io.circe.DecodingFailure, T]
) { self =>

  def map[B](f: T => B): Request[B, Auth] =
    copy(responseAs = responseAs.andThen(_.map(f)))

  def transform[B](f: T => Either[io.circe.DecodingFailure, B]): Request[B, Auth] =
    copy(responseAs = responseAs.andThen(_.flatMap(f)))

  def as[B: Decoder: IsOption]: Request[B, Auth] =
    copy(responseAs = implicitly[Decoder[B]].decodeJson)

  def at[B: IsOption](key: String)(implicit ev: Decoder[B]): Request[B, Auth] =
    copy(responseAs = extractBodyAt[B](key).decodeJson)

  def auth: RequestAuth[T] =
    RequestAuth(self)

  def jsonBody[A: Encoder](json: A): Request[T, Auth] = copy(body = SlackBody.JsonBody(json.asJson))

  def formBody[A: FormEncoder](form: A): Request[T, Auth] =
    copy(body = SlackBody.MixedBody(implicitly[FormEncoder[A]].encode(form).toMap))

  def formBody(form: (String, SlackParamMagnet)*): Request[T, Auth]                          =
    formBody(form.toList)
  def formBody(form: List[(String, SlackParamMagnet)]): Request[T, Auth]                     = formBody(
    form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap
  )
  def formBody(form: Map[String, String]): Request[T, Auth]                                  = copy(body = SlackBody.MixedBody(form))
  def entityBody(entity: RequestEntity)(form: (String, SlackParamMagnet)*): Request[T, Auth] =
    copy(body = SlackBody.MixedBody(form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap, Some(entity)))
  def partBody(parts: List[Part[BasicRequestBody]]): Request[T, Auth]                        = copy(body = SlackBody.PartBody(parts))

  def toRequest[T1 >: T: IsOption](
    baseUri: String
  ): RequestT[Identity, SlackResponse[T1], Any] = {
    val respond: ResponseAs[SlackResponse[T], Any] =
      respondWith(
        deserializeJson(SlackResponse.decodeWith(responseAs), implicitly[IsOption[SlackResponse[T]]])
      ).mapWithMetadata {
        // Special case of a rate limit
        case (_, meta) if meta.code == StatusCode.TooManyRequests =>
          SlackError.RatelimitError(
            meta.headers("Retry-After").headOption.map(_.toLong.seconds).getOrElse(Duration.Zero)
          )
        case (Left(DeserializationException(_, error)), _)        => SlackError.fromThrowable(error)
        case (Left(HttpError(body, code)), _)                     => SlackError.HttpError(code.code, body)
        case (Right(response), _)                                 => response
      }

    (body match {
      case SlackBody.JsonBody(json)                =>
        basicRequest.post(uri"$baseUri/${method.name}").body(json.deepDropNullValues)
      case SlackBody.MixedBody(form, Some(entity)) =>
        entity(basicRequest.post(uri"$baseUri/${method.name}?$form"))
      case SlackBody.MixedBody(form, _)            =>
        basicRequest.post(uri"$baseUri/${method.name}").body(form)
      case SlackBody.PartBody(parts)               =>
        basicRequest.post(uri"$baseUri/${method.name}").multipartBody(parts)
    }).tag("Slack-MethodName", method.name).response(respond)
  }
}

object Request {

  private def extractBodyAt[A: Decoder](key: String): Decoder[A] =
    Decoder.instance[A](_.downField(key).as[A])

  private def respondWith[A](
    f: String => Either[io.circe.Error, A]
  ): ResponseAs[Either[ResponseException[String, circe.Error], A], Any] =
    asString.mapWithMetadata(ResponseAs.deserializeRightWithError(f)).showAsJson

  def make(method: MethodName, body: SlackBody = SlackBody.empty): Request[Unit, Unit] =
    Request(method, body, _ => Right(()))

}
