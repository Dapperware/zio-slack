package com.github.dapperware.slack

import com.github.dapperware.slack.AuthenticationToken.{
  TokenAuthenticateWithPartiallyApplied,
  TokenAuthenticateWithZIOPartiallyApplied
}
import zio.{ Tag, ZIO, ZLayer }

private[slack] trait AuthenticationToken[T] {
  type Token = T

  def authenticateWithZIO[R, E](token: => ZIO[R, E, Token]): TokenAuthenticateWithZIOPartiallyApplied[Token, R, E] =
    new TokenAuthenticateWithZIOPartiallyApplied[Token, R, E](token)

  def authenticateWith(token: Token): TokenAuthenticateWithPartiallyApplied[Token] =
    new TokenAuthenticateWithPartiallyApplied[Token](token)

}

object AuthenticationToken {
  final class TokenAuthenticateWithZIOPartiallyApplied[Token, R, E](token: => ZIO[R, E, Token]) {
    def apply[R1 <: R, E1 >: E, A](f: => ZIO[Token with R1, E1, A])(implicit
      tag: Tag[Token]
    ): ZIO[R1, E1, A] =
      f.provideSomeLayer[R1](ZLayer(token))
  }

  final class TokenAuthenticateWithPartiallyApplied[Token](val token: Token) {
    def apply[R, E, A](f: => ZIO[Token with R, E, A])(implicit tag: Tag[Token]): ZIO[R, E, A] =
      f.provideSomeEnvironment[R](_.add[Token](token))
  }
}
