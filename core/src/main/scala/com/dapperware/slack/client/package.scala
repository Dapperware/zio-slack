package com.dapperware.slack

import sttp.client._
import sttp.client.asynchttpclient.zio.SttpClient
import zio.{ Has, ZIO, ZLayer }

package object client {
  type SlackClient = Has[SlackClient.Service]

  object SlackClient {
    trait Service {
      def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T]
    }

    val any = ZLayer.requires[SlackClient]

    def live: ZLayer[SttpClient, Nothing, SlackClient] =
      ZLayer.fromFunction[SttpClient, SlackClient.Service](
        client =>
          new Service {
            override def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T] =
              for {
                result <- client.get.send(request)
                json   <- ZIO.fromEither(result.body)
              } yield json
        }
      )
  }

  def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[SlackClient, Throwable, T] =
    ZIO.accessM(_.get.send(request))

}
