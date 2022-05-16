package com.github.dapperware

import zio.{ URIO, ZIO }

package object slack {
  implicit class EnrichedApiCall[-R, +A](val response: URIO[R, SlackResponse[A]]) extends AnyVal {
    def isOk: ZIO[R, Nothing, Boolean] =
      response.map(_.isOk)

    def value: ZIO[R, SlackError, A] =
      response.map(_.toEither).absolve
  }
}
