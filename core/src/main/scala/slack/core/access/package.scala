package slack.core

import slack.core.access.AccessToken.Service
import sttp.client.RequestT
import zio.{ Has, UIO, URIO, ZIO, ZLayer }

package object access {
  type AccessToken = Has[AccessToken.Service]

  type ClientToken = Has[AccessToken.Token]

  object secret {
    type ClientSecret = Has[ClientSecret.Secret]
    object ClientSecret {

      trait Secret extends Service {
        def clientId: UIO[String]
        def clientSecret: UIO[String]
      }

      def live(id: String, secret: String): ZLayer.NoDeps[Nothing, ClientSecret] = ZLayer.succeed {
        new Secret {
          override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Any, RequestT[U, T, S]] =
            UIO.succeed(request.auth.basic(id, secret))

          override def clientId: UIO[String] =
            UIO.succeed(id)

          override def clientSecret: UIO[String] =
            UIO.succeed(secret)
        }
      }

      val any: ZLayer[ClientSecret, Nothing, ClientSecret] = ZLayer.requires[ClientSecret]
    }

    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
      ZIO.accessM[ClientSecret](_.get.authenticateM(request))

    def clientId: URIO[ClientSecret, String] =
      ZIO.accessM(_.get.clientId)

    def clientSecret: URIO[ClientSecret, String] =
      ZIO.accessM(_.get.clientSecret)
  }

  object AccessToken {
    trait Service {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]]
    }

    trait Token extends Service {
      def get: UIO[String]
    }

    val any: ZLayer[AccessToken, Nothing, AccessToken] =
      ZLayer.requires[AccessToken]

    def live(token: String): ZLayer.NoDeps[Nothing, ClientToken] = ZLayer.succeed {
      new Token {
        override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
          UIO.succeed(request.auth.bearer(token))

        override def get: UIO[String] = UIO.succeed(token)
      }
    }

  }

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientToken, RequestT[U, T, S]] =
    ZIO.accessM[ClientToken](_.get.authenticateM(request))

  def get: URIO[ClientToken, String] =
    ZIO.accessM(_.get.get)

}
