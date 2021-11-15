package com.github.dapperware.slack

import zio.{ Has, ZIO }

final class WithAccessPartially private[slack] (private val token: String) extends AnyVal {
  def apply[R <: Has[_], E, A](effect: ZIO[R with Has[AccessToken], E, A]): ZIO[R, E, A] =
    effect.provideSomeLayer[R](AccessToken.make(token).toLayer)
}
