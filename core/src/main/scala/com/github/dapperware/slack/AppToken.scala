package com.github.dapperware.slack

import zio.{ UIO, ZIO }

case class AppToken(token: String)
object AppToken {
  def make(token: String): UIO[AppToken] = ZIO.succeed(AppToken(token))
}
