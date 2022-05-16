package com.github.dapperware.slack

import com.github.dapperware.slack.Request.{ extractBodyAt, respondWith }
import com.github.dapperware.slack.client.RequestEntity
import io.circe
import io.circe.{ Decoder, Json }
import sttp.client3
import sttp.client3.circe._
import sttp.client3.json._
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
import zio.duration.{ durationLong, Duration }

case class MethodName(name: String) extends AnyVal

case class Request[+T, Auth](
  method: MethodName,
  body: SlackBody,
  responseAs: Json => Either[io.circe.Error, T]
) { self =>

  def map[B](f: T => B): Request[B, Auth] =
    copy(responseAs = responseAs.andThen(_.map(f)))

  def transform[B](f: T => Either[io.circe.Error, B]): Request[B, Auth] =
    copy(responseAs = responseAs.andThen(_.flatMap(f)))

  def as[B: Decoder: IsOption]: Request[B, Auth] =
    copy(responseAs = implicitly[Decoder[B]].decodeJson)

  def at[B: IsOption](key: String)(implicit ev: Decoder[B]): Request[B, Auth] =
    copy(responseAs = extractBodyAt[B](key).decodeJson)

  def auth[A]: Request[T, A] = self.asInstanceOf[Request[T, A]]

  def jsonBody(json: Json): Request[T, Auth]                                                 = copy(body = SlackBody.JsonBody(json))
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
      respondWith(deserializeJson(SlackResponse.decodeWith(responseAs), implicitly)).mapWithMetadata {
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
        basicRequest.post(uri"$baseUri/${method.name}").body(json)
      case SlackBody.MixedBody(form, Some(entity)) =>
        entity(basicRequest.post(uri"$baseUri/${method.name}?$form"))
      case SlackBody.MixedBody(form, _)            =>
        basicRequest.post(uri"$baseUri/${method.name}").body(form)
      case SlackBody.PartBody(parts)               =>
        basicRequest.post(uri"$baseUri/${method.name}").multipartBody(parts)
    }).response(respond)
  }
}

object Request {

  private def extractBodyAt[A: Decoder](key: String): Decoder[A] =
    Decoder.instance[A](_.downField(key).as[A])

  private def respondWith[A](
    f: String => Either[io.circe.Error, A]
  ): ResponseAs[Either[ResponseException[String, circe.Error], A], Any] =
    asString.mapWithMetadata(ResponseAs.deserializeRightWithError(f)).showAsJson

  def make(method: MethodName, body: SlackBody): Request[Unit, AccessToken] =
    Request(method, body, _ => Right(()))

  def make(method: String, body: SlackBody = SlackBody.empty): Request[Unit, AccessToken] =
    make(MethodName(method), body)

}

sealed trait SlackBody

object SlackBody {
  val empty: SlackBody                                                                          = fromForm(Map.empty[String, String])
  def fromJson(json: Json): SlackBody                                                           = SlackBody.JsonBody(json)
  def fromForm(first: (String, SlackParamMagnet), rest: (String, SlackParamMagnet)*): SlackBody = fromForm(
    first +: rest.toList
  )
  def fromForm(form: List[(String, SlackParamMagnet)]): SlackBody                               =
    SlackBody.MixedBody(form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap, None)
  def fromForm(form: Map[String, String]): SlackBody                                            = SlackBody.MixedBody(form)
  def fromMixed(form: Map[String, String], entity: RequestEntity): SlackBody                    = SlackBody.MixedBody(form, Some(entity))
  def fromParts(parts: List[Part[BasicRequestBody]]): SlackBody                                 = SlackBody.PartBody(parts)

  case class JsonBody(json: Json)                                                       extends SlackBody
  case class PartBody(parts: List[Part[BasicRequestBody]])                              extends SlackBody
  case class MixedBody(form: Map[String, String], entity: Option[RequestEntity] = None) extends SlackBody
}
