package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.UIO

object ClientSecret {
  case class Token(clientId: String, clientSecret: String) {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
      UIO.succeed(request.auth.basic(clientId, clientSecret))
  }

  def make(clientId: String, clientSecret: String): UIO[ClientSecret.Token] =
    UIO.succeed(ClientSecret.Token(clientId, clientSecret))

}
