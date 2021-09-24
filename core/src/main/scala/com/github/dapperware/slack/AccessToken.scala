package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ UIO, URIO, Has, ZIO }

final case class Token(token: String)
object AccessToken {

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[Has[Token], RequestT[U, T, S]] =
    ZIO.serviceWith[Token](token => UIO.succeed(request.auth.bearer(token.token)))
  

  def make(token: String): UIO[Token] = UIO.succeed(Token(token))
}
