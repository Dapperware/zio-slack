package com.github.dapperware.slack

import zio.{ Tag, UIO, URIO, ZIO }

trait SlackClient {
  def apiCall[T](request: Request[T, Unit]): UIO[SlackResponse[T]]

  def apiCall[T, A](request: Request[T, A])(implicit ev: HasAuth[A], tag: Tag[A]): URIO[A, SlackResponse[T]]
}

object SlackClient {
  def apiCall[T](request: Request[T, Unit]): URIO[SlackClient, SlackResponse[T]] =
    ZIO.serviceWithZIO(_.apiCall(request))

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A]): URIO[SlackClient with A, SlackResponse[T]] =
    ZIO.serviceWithZIO[SlackClient](_.apiCall[T, A](request))
}
