package com.github.dapperware.slack

import zio.UIO

case class AppToken(token: String)
object AppToken {
  def make(token: String): UIO[AppToken] = UIO.succeed(AppToken(token))
}
