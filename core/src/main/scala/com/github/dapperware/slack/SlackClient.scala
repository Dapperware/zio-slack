package com.github.dapperware.slack

import zio.{ Has, Tag, UIO, URIO, ZIO }

trait SlackClient {
  def apiCall[T](request: Request[T, Unit]): UIO[SlackResponse[T]]

  def apiCall[T, A](request: Request[T, A])(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[A], SlackResponse[T]]
}

object SlackClient {
  def apiCall[T](request: Request[T, Unit]): URIO[Has[SlackClient], SlackResponse[T]] =
    ZIO.serviceWith(_.apiCall(request))

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[SlackClient] with Has[A], SlackResponse[T]] =
    ZIO.accessM(_.get[SlackClient].apiCall[T, A](request))
}
