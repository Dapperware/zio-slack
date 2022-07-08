package com.github.dapperware.slack

import zio.{ ZIO, ZLayer }

final class WithAccessPartially private[slack] (private val token: String) extends AnyVal {
  def apply[R, E, A](effect: ZIO[R with AccessToken, E, A]): ZIO[R, E, A] =
    effect.provideSome[R](ZLayer(AccessToken.make(token)))
}
