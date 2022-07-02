package com.github.dapperware.slack

import sttp.client3.RequestT

case class RequestAuth[+T] private (request: Request[T, _]) {
  def clientSecret: Request[T, ClientSecret] = request.asInstanceOf[Request[T, ClientSecret]]
  def accessToken: Request[T, AccessToken]   = request.asInstanceOf[Request[T, AccessToken]]
  def none: Request[T, Unit]                 = request.asInstanceOf[Request[T, Unit]]

}

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

}
