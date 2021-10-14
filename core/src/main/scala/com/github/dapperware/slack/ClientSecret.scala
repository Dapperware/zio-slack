package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{UIO, ZIO, URIO}

final case class ClientSecretToken(clientId: String, clientSecret: String)

object ClientSecretToken {
  
  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
    ZIO.serviceWith[ClientSecretToken](clientSecretToken => UIO.succeed(request.auth.basic(clientSecretToken.clientId, clientSecretToken.clientSecret)))
  

  def make(clientId: String, clientSecret: String): UIO[ClientSecretToken] =
    UIO.succeed(ClientSecretToken(clientId, clientSecret))

}
