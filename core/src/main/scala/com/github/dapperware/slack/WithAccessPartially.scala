package com.github.dapperware.slack

import zio.{ Trace, ZIO, ZLayer }

final class WithAccessPartially private[slack] (private val token: String) extends AnyVal {
  def apply[R, E, A](effect: ZIO[R with AccessToken, E, A])(implicit trace: Trace): ZIO[R, E, A] =
    effect.provideSome[R](ZLayer(AccessToken.make(token)))
}
