package com.github.dapperware.slack

case class AppToken(token: String)
object AppToken extends AuthenticationToken[AppToken]
