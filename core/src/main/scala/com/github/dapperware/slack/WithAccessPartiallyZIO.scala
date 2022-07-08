package com.github.dapperware.slack

import zio.{ ZIO, ZLayer }

final class WithAccessPartiallyZIO[R1, +E] private[slack] (private val token: ZIO[R1, E, AccessToken]) extends AnyVal {
  def apply[R0 <: R1, R <: R0 with AccessToken, E1 >: E, A](
    effect: ZIO[R, E1, A]
  )(implicit ev: R0 with AccessToken <:< R): ZIO[R0, E1, A] =
    effect.provideSomeLayer[R0](ZLayer(token))
}
