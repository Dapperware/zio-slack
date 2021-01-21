package com.github.dapperware.slack

import sttp.client.RequestT
import zio.{ UIO, URIO }

object AccessToken {

  final case class Token private (token: String) {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
      UIO.succeed(request.auth.bearer(token))
  }

  def make(token: String): UIO[AccessToken.Token] = UIO.succeed(Token(token))
}
