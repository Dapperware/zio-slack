package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ Has, UIO, URIO, ZIO }

final case class AccessToken(token: String)
object AccessToken {

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Has[AccessToken], RequestT[U, T, S]] =
    ZIO.serviceWith[AccessToken](token => UIO.succeed(request.auth.bearer(token.token)))

  def make(token: String): UIO[AccessToken] = UIO.succeed(AccessToken(token))
}
