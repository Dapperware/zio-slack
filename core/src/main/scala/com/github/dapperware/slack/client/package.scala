package com.github.dapperware.slack

import sttp.client3.asynchttpclient.zio.{ SttpClient, send => sendRequest }
import sttp.client3.{ Request, ResponseException }
import zio.{ Has, ZIO, ZLayer }

package object client {
  type SlackClient = Has[SlackClient.Service]

  object SlackClient {
    trait Service {
      def send[T, E](request: Request[Either[ResponseException[String, E], T], Any]): ZIO[Any, Throwable, T]
    }

    val any = ZLayer.requires[SlackClient]

    def live: ZLayer[SttpClient, Nothing, SlackClient] =
      ZLayer.fromFunction[SttpClient, SlackClient.Service](client =>
        new Service {
          override def send[T, E](
            request: Request[Either[ResponseException[String, E], T], Any]
          ): ZIO[Any, Throwable, T] =
            for {
              result <- sendRequest(request).provide(client)
              json   <- ZIO.fromEither(result.body)
            } yield json
        }
      )
  }

  def send[T, E](request: Request[Either[ResponseException[String, E], T], Any]): ZIO[SlackClient, Throwable, T] =
    ZIO.accessM(_.get.send(request))

}
