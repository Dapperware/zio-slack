package com.github.dapperware.slack

import zio.{ Trace, URIO, ZIO, ZLayer }

final case class ClientSecret(clientId: String, clientSecret: String)

object ClientSecret extends AuthenticationToken[ClientSecret] {

  val get: URIO[ClientSecret, ClientSecret] =
    ZIO.service[ClientSecret]

  val clientId: URIO[ClientSecret, String] = ZIO.serviceWith(_.clientId)

  val clientSecret: URIO[ClientSecret, String] = ZIO.serviceWith(_.clientSecret)

}
