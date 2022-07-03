package com.github.dapperware.slack

case class RequestAuth[+T] private (request: Request[T, _]) {
  def clientSecret: Request[T, ClientSecret] = request.asInstanceOf[Request[T, ClientSecret]]
  def accessToken: Request[T, AccessToken]   = request.asInstanceOf[Request[T, AccessToken]]
  def none: Request[T, Unit]                 = request.asInstanceOf[Request[T, Unit]]

}
