package slack

import sttp.client.RequestT
import zio.{UIO, URIO}

trait AccessToken {
  val accessToken: AccessToken.Service[Any]
}

object AccessToken {
  trait Service[R] {
    def authenticate[U[_], T, S](request: RequestT[U, T, S]): URIO[R, RequestT[U, T, S]]
  }

  def make(token: String): UIO[AccessToken] =
    UIO.succeed(new AccessToken {
      override val accessToken: Service[Any] = new Service[Any] {
        override def authenticate[U[_], T, S](request: RequestT[U, T, S]): URIO[Any, RequestT[U, T, S]] =
          UIO.succeed(request.auth.bearer(token))
      }
    })
}
