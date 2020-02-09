package slack.core

import sttp.client.RequestT
import zio._

trait AccessToken {
  val accessToken: AccessToken.Service
}

object AccessToken {
  trait Service {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]]
    def get: UIO[String]
  }

  def make(token: String): UIO[AccessToken] =
    UIO.succeed(new AccessToken {
      override val accessToken: Service = new Service {
        override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
          UIO.succeed(request.auth.bearer(token))

        override def get: UIO[String] = UIO.succeed(token)
      }
    })

  def makeManaged(token: String): Managed[Nothing, AccessToken] =
    make(token).toManaged_

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
    ZIO.accessM[AccessToken](_.accessToken.authenticateM(request))

  def get: URIO[AccessToken, String] =
    ZIO.accessM(_.accessToken.get)

}
