package com.github.dapperware.slack

import sttp.client3.asynchttpclient.zio.{ SttpClient, send => sendRequest }
import sttp.client3.{ Request, Response, ResponseException }
import sttp.model.StatusCode
import zio.{ Has, Task, ZIO, ZLayer }
import zio.duration._

import scala.util.Try

trait SlackClient {
  def send[T, E](request: Request[Either[ResponseException[String, E], T], Any]): Task[T]
}

object SlackClient {
  val any = ZLayer.requires[SlackClient]

  def make = (for {
    client <- ZIO.environment[SttpClient]
  } yield new SlackClient {
    override def send[T, E](
      request: Request[Either[ResponseException[String, E], T], Any]
    ): ZIO[Any, Throwable, T] =
      for {
        result <- sendRequest(request).provide(client)
        json   <- result match {
                    // Special case handling of ratelimiting
                    case Response(_, code, _, headers, _, _) if code == StatusCode.TooManyRequests =>
                      val retryAfter = headers
                        .find(_.is("Retry-After"))
                        .map(_.value)
                        .flatMap(v => Try(v.toInt).toOption.map(_.seconds))
                      ZIO.fail(SlackException.RatelimitError(retryAfter))
                    case Response(body, _, _, _, _, _)                                             =>
                      ZIO.fromEither(body)
                  }
      } yield json
  })

  def live: ZLayer[SttpClient, Nothing, Has[SlackClient]] =
    make.toLayer
}
