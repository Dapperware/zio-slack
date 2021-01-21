package com.github.dapperware.slack

import zio.{ Has, ZIO }

final class WithAccessPartiallyM[R1 <: Has[_], +E] private[slack] (private val token: ZIO[R1, E, AccessToken.Token])
    extends AnyVal {
  def apply[R0 <: R1, R <: R0 with AccessToken, E1 >: E, A](
    effect: ZIO[R, E1, A]
  )(implicit ev: R0 with AccessToken <:< R): ZIO[R0, E1, A] =
    effect.provideSomeLayer[R0](token.toLayer)
}
