package slack.core

import sttp.client.RequestT
import zio.{ Managed, UIO, URIO, ZIO }

trait ClientSecret {
  val clientSecret: ClientSecret.Service
}

object ClientSecret {
  trait Service {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Any, RequestT[U, T, S]]
    def clientId: UIO[String]
    def clientSecret: UIO[String]
  }

  def make(id: String, secret: String): UIO[ClientSecret] =
    UIO.succeed(new ClientSecret {
      override val clientSecret: Service = new Service {
        override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Any, RequestT[U, T, S]] =
          UIO.succeed(request.auth.basic(id, secret))

        override def clientId: UIO[String] =
          UIO.succeed(id)

        override def clientSecret: UIO[String] =
          UIO.succeed(secret)
      }
    })

  def makeManaged(id: String, secret: String): Managed[Nothing, ClientSecret] =
    make(id, secret).toManaged_

  def authenticateM[R, U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
    ZIO.accessM[ClientSecret](_.clientSecret.authenticateM(request))

  def clientId: URIO[ClientSecret, String] =
    ZIO.accessM(_.clientSecret.clientId)

  def clientSecret: URIO[ClientSecret, String] =
    ZIO.accessM(_.clientSecret.clientSecret)
}
