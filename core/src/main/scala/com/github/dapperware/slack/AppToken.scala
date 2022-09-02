package com.github.dapperware.slack

import zio.{ Trace, UIO, ZIO }

case class AppToken(token: String)
object AppToken {
  def make(token: String)(implicit trace: Trace): UIO[AppToken] = ZIO.succeed(AppToken(token))
}
