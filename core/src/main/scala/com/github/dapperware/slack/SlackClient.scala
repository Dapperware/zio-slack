package com.github.dapperware.slack

import zio.{ Tag, Trace, UIO, URIO, ZIO }

trait SlackClient {
  def apiCall[T](request: Request[T, Unit])(implicit trace: Trace): UIO[SlackResponse[T]]

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[A, SlackResponse[T]]
}

object SlackClient {
  def apiCall[T](request: Request[T, Unit])(implicit trace: Trace): URIO[SlackClient, SlackResponse[T]] =
    ZIO.serviceWithZIO(_.apiCall(request))

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[SlackClient with A, SlackResponse[T]] =
    ZIO.serviceWithZIO[SlackClient](_.apiCall[T, A](request))
}
