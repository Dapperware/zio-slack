package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ UIO, URIO, ZIO }

final case class AccessToken(token: String)
object AccessToken {

  def authenticateZIO[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
    ZIO.serviceWith[AccessToken](token => request.auth.bearer(token.token))

  def make(token: String): UIO[AccessToken] = ZIO.succeed(AccessToken(token))
}
