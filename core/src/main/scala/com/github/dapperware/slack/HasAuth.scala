package com.github.dapperware.slack

import sttp.client3.RequestT

sealed trait HasAuth[-A] {
  def apply[U[_], T, S](req: RequestT[U, T, S])(a: A): RequestT[U, T, S]
}

object HasAuth {

  implicit val clientSecretAuth: HasAuth[ClientSecret] = new HasAuth[ClientSecret] {
    override def apply[U[_], T, S](req: RequestT[U, T, S])(a: ClientSecret): RequestT[U, T, S] =
      req.auth.basic(a.clientId, a.clientSecret)
  }

  implicit val accessTokenAuth: HasAuth[AccessToken] = new HasAuth[AccessToken] {
    override def apply[U[_], T, S](req: RequestT[U, T, S])(a: AccessToken): RequestT[U, T, S] =
      req.auth.bearer(a.token)
  }

  implicit val appTokenAuth: HasAuth[AppToken] = new HasAuth[AppToken] {
    override def apply[U[_], T, S](req: RequestT[U, T, S])(a: AppToken): RequestT[U, T, S] =
      req.auth.bearer(a.token)
  }

}
