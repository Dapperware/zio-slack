package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ UIO, URIO, ZIO }

final case class ClientSecret(clientId: String, clientSecret: String)

object ClientSecret {

  def authenticateM[U[_], T](request: RequestT[U, T, Any]): URIO[ClientSecret, RequestT[U, T, Any]] =
    ZIO.serviceWith[ClientSecret](clientSecretToken =>
      request.auth.basic(clientSecretToken.clientId, clientSecretToken.clientSecret)
    )

  def make(clientId: String, clientSecret: String): UIO[ClientSecret] =
    ZIO.succeed(ClientSecret(clientId, clientSecret))

  val get: URIO[ClientSecret, ClientSecret] =
    ZIO.service[ClientSecret]

  val clientId: URIO[ClientSecret, String] = ZIO.serviceWith(_.clientId)

  val clientSecret: URIO[ClientSecret, String] = ZIO.serviceWith(_.clientSecret)

}
