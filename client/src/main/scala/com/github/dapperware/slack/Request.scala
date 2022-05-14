package com.github.dapperware.slack

import com.github.dapperware.slack.Request.{extractBodyAt, respondWith}
import com.github.dapperware.slack.client.RequestEntity
import io.circe.{Decoder, Json}
import sttp.client3.circe._
import sttp.client3.json._
import sttp.client3.{BasicRequestBody, DeserializationException, HttpError, Identity, IsOption, RequestT, ResponseAs, UriContext, asString, basicRequest}
import sttp.model.{Part, StatusCode}
import zio.duration.{Duration, durationLong}

case class MethodName(name: String) extends AnyVal

case class Request[+A](
  method: MethodName,
  body: SlackBody,
  responseAs: Json => Either[io.circe.Error, A]) {

  def map[B](f: A => B): Request[B] =
    copy(responseAs = responseAs.andThen(_.map(f)))

  def transform[B](f: A => Either[io.circe.Error, B]): Request[B] =
    copy(responseAs = responseAs.andThen(_.flatMap(f)))

  def as[B: Decoder: IsOption]: Request[B] =
    Request(method, body, implicitly[Decoder[B]].decodeJson )

  def at[B: IsOption](key: String)(implicit ev: Decoder[B]): Request[B] =
    Request(method, body, extractBodyAt[B](key).decodeJson)

  def jsonBody(json: Json): Request[A] = copy(body = SlackBody.JsonBody(json))
  def formBody(first: (String, SlackParamMagnet), rest: (String, SlackParamMagnet)*): Request[A] =
    formBody(first +: rest.toList)
  def formBody(form: List[(String, SlackParamMagnet)]): Request[A] = formBody(form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap)
  def formBody(form: Map[String, String]): Request[A] = copy(body = SlackBody.MixedBody(form))
  def entityBody(form: Map[String, String], entity: RequestEntity): Request[A] = copy(body = SlackBody.MixedBody(form, Some(entity)))
  def partBody(parts: List[Part[BasicRequestBody]]): SlackBody = SlackBody.PartBody(parts)

  def toRequest[A1 >: A: IsOption](baseUri: String): RequestT[Identity, SlackResponse[A1], Any] = {
    val respond: ResponseAs[SlackResponse[A], Any] = respondWith(deserializeJson(SlackResponse.decodeWith(responseAs), implicitly)).mapWithMetadata {
      // Special case of a rate limit
      case (_, meta) if meta.code == StatusCode.TooManyRequests =>
        SlackError.RatelimitError(meta.headers("Retry-After").headOption.map(_.toLong.seconds).getOrElse(Duration.Zero))
      case (Left(DeserializationException(_, error)), _) => SlackError.fromThrowable(error)
      case (Left(HttpError(body, code)), _) => SlackError.HttpError(code.code, body)
      case (Right(response), _) => response
    }

    (body match {
      case SlackBody.JsonBody(json) =>
        basicRequest.post(uri"$baseUri/${method.name}").body(json)
      case SlackBody.MixedBody(form, Some(entity)) =>
        entity(basicRequest.post(uri"$baseUri/${method.name}?$form"))
      case SlackBody.MixedBody(form, _) =>
        basicRequest.post(uri"$baseUri/${method.name}").body(form)
      case SlackBody.PartBody(parts) =>
        basicRequest.post(uri"$baseUri/${method.name}").multipartBody(parts)
    }).response(respond)
  }
}

object Request {

  private def extractBodyAt[A: Decoder](key: String): Decoder[A] =
    Decoder.instance[A] { _.downField(key).as[A] }

  private def respondWith[A](f: String => Either[io.circe.Error, A]) =
    asString.mapWithMetadata(ResponseAs.deserializeRightWithError(f)).showAsJson

  def make(method: MethodName, body: SlackBody): Request[Unit] =
    Request(method, body, _ => Right(()))

  def make(method: String, body: SlackBody = SlackBody.empty): Request[Unit] =
    make(MethodName(method), body)

}

sealed trait SlackBody

object SlackBody {
  val empty: SlackBody = fromForm(Map.empty[String, String])
  def fromJson(json: Json): SlackBody = SlackBody.JsonBody(json)
  def fromForm(first: (String, SlackParamMagnet), rest: (String, SlackParamMagnet)*): SlackBody = fromForm(first +: rest.toList)
  def fromForm(form: List[(String, SlackParamMagnet)]): SlackBody = SlackBody.MixedBody(form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap, None)
  def fromForm(form: Map[String, String]): SlackBody = SlackBody.MixedBody(form)
  def fromMixed(form: Map[String, String], entity: RequestEntity): SlackBody = SlackBody.MixedBody(form, Some(entity))
  def fromParts(parts: List[Part[BasicRequestBody]]): SlackBody = SlackBody.PartBody(parts)

  case class JsonBody(json: Json) extends SlackBody
  case class PartBody(parts: List[Part[BasicRequestBody]]) extends SlackBody
  case class MixedBody(form: Map[String, String], entity: Option[RequestEntity] = None) extends SlackBody
}