package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ Has, UIO, URIO, ZIO }

final case class ClientSecret(clientId: String, clientSecret: String)

object ClientSecret {

  def authenticateM[U[_], T](request: RequestT[U, T, Any]): URIO[Has[ClientSecret], RequestT[U, T, Any]] =
    ZIO.serviceWith[ClientSecret](clientSecretToken =>
      UIO.succeed(request.auth.basic(clientSecretToken.clientId, clientSecretToken.clientSecret))
    )

  def make(clientId: String, clientSecret: String): UIO[ClientSecret] =
    UIO.succeed(ClientSecret(clientId, clientSecret))

  val get: URIO[Has[ClientSecret], ClientSecret] =
    ZIO.service[ClientSecret]

  val clientId: URIO[Has[ClientSecret], String] = ZIO.access(_.get.clientId)

  val clientSecret: URIO[Has[ClientSecret], String] = ZIO.access(_.get.clientSecret)

}
