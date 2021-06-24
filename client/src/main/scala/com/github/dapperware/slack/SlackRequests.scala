package com.github.dapperware.slack

import com.github.dapperware.slack.client.RequestEntity
import io.circe
import io.circe.Json
import sttp.client3.circe._
import sttp.client3._
import zio.{ UIO, ZIO }

trait SlackRequests {
  type SlackResponse[T] = Either[ResponseException[String, circe.Error], T]

  def requestJson(method: String, body: Json): UIO[Request[SlackResponse[Json], Any]] =
    UIO.succeed(
      basicRequest
        .post(uri"https://slack.com/api/$method")
        .body(body.deepDropNullValues)
        .response(asJson[Json])
    )

  def request(method: String, params: (String, SlackParamMagnet)*): UIO[Request[SlackResponse[Json], Any]] =
    UIO.succeed(
      basicRequest
        .post(uri"https://slack.com/api/$method")
        .body(List(params.map(p => p._2.produce.map(p._1 -> _)): _*).flatten.toMap)
        .response(asJson[Json])
    )

  def requestQueryParams(method: String, params: Seq[(String, String)]): UIO[Request[SlackResponse[Json], Any]] = {
    val uri = uri"https://slack.com/api/$method"
      .addParams(params:_*)
    UIO.succeed(
      basicRequest
        .post(uri)
        .response(asJson[Json])
    )
  }

  def requestEntity(method: String, params: (String, SlackParamMagnet)*)(
    body: RequestEntity
  ): UIO[Request[SlackResponse[Json], Any]] = UIO.effectTotal(
    body(basicRequest)
      .post(uri"https://slack.com/api/$method?$params")
      .response(asJson[Json])
  )

  def sendM[T](request: UIO[Request[SlackResponse[T], Any]]): ZIO[SlackEnv, Throwable, T] =
    request >>= authenticateM >>= client.send[T, circe.Error]

}
