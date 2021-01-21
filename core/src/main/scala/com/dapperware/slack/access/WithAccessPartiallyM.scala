package com.dapperware.slack.access

import zio.{ Has, IO, ZIO }

// TODO Is there a way for token to also have an `R` and only eliminate the `AccessToken` requirement?
class WithAccessPartiallyM[+E] private[access] (token: IO[E, AccessToken.Token]) {
  def apply[R0 <: Has[_], R <: R0 with AccessToken, E1 >: E, A](
    effect: ZIO[R, E1, A]
  )(implicit ev: R0 with AccessToken <:< R): ZIO[R0, E1, A] =
    effect.provideSomeLayer[R0](token.toLayer)
}

class WithAccessPartially private[slack] (val token: String) extends AnyVal {
  def apply[R <: Has[_], E, A](effect: ZIO[R with AccessToken, E, A]): ZIO[R, E, A] =
    effect.provideSomeLayer[R](AccessToken.make(token).toLayer)
}
