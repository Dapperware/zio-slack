package slack

import sttp.client.RequestT
import zio.{ Has, URIO, ZIO }

package object access {
  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Has[AccessToken], RequestT[U, T, S]] =
    ZIO.accessM[Has[AccessToken]](_.get.authenticateM(request))

  object secret {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Has[ClientSecret], RequestT[U, T, S]] =
      ZIO.accessM[Has[ClientSecret]](_.get.authenticateM(request))
  }
}
