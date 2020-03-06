package slack.core

import sttp.client.RequestT
import zio.{ Has, UIO, URIO, ZIO, ZLayer }

package object access {
  type AccessToken  = Has[AccessToken.Service]
  type ClientSecret = Has[AccessToken.Secret]
  type ClientToken  = Has[AccessToken.Token]

  object AccessToken {
    trait Service {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]]
    }

    trait Token extends Service {
      def get: UIO[String]
    }

    trait Secret extends Service {
      def clientId: UIO[String]
      def clientSecret: UIO[String]
    }

    val any: ZLayer[AccessToken, Nothing, AccessToken] =
      ZLayer.requires[AccessToken]

    def liveClient(token: String): ZLayer.NoDeps[Nothing, ClientToken] = ZLayer.succeed {
      new Token {
        override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
          UIO.succeed(request.auth.bearer(token))

        override def get: UIO[String] = UIO.succeed(token)
      }
    }

    def liveSecret(id: String, secret: String): ZLayer.NoDeps[Nothing, ClientSecret] = ZLayer.succeed {
      new Secret {
        override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Any, RequestT[U, T, S]] =
          UIO.succeed(request.auth.basic(id, secret))

        override def clientId: UIO[String] =
          UIO.succeed(id)

        override def clientSecret: UIO[String] =
          UIO.succeed(secret)
      }
    }

  }

  object secret {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
      ZIO.accessM[ClientSecret](_.get.authenticateM(request))
  }

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientToken, RequestT[U, T, S]] =
    ZIO.accessM[ClientToken](_.get.authenticateM(request))

  def get: URIO[ClientToken, String] =
    ZIO.accessM(_.get.get)

  def clientId: URIO[ClientSecret, String] =
    ZIO.accessM(_.get.clientId)

  def clientSecret: URIO[ClientSecret, String] =
    ZIO.accessM(_.get.clientSecret)

}
