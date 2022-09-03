package com.github.dapperware.slack

import zio.{ Tag, Trace, URIO, ZIO }

trait SlackClient {

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[A, SlackResponse[T]]
}

object SlackClient {

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[SlackClient with A, SlackResponse[T]] =
    ZIO.serviceWithZIO[SlackClient](_.apiCall[T, A](request))
}
