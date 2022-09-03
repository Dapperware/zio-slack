package com.github.dapperware.slack

final case class AccessToken(token: String)
object AccessToken extends AuthenticationToken[AccessToken]
