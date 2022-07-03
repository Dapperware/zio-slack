package com.github.dapperware.slack

import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClient.Service
import sttp.client3.{ Identity, RequestT }
import zio.{ Has, Tag, UIO, URIO, ZIO, ZLayer }

class HttpSlack private (baseUrl: String, client: SttpClient.Service) extends SlackClient {

  private def makeCall[A](request: RequestT[Identity, SlackResponse[A], Any]): URIO[Any, SlackResponse[A]] =
    client
      .send(request)
      .mapBoth(SlackError.fromThrowable, _.body)
      .merge

  def apiCall[T](request: Request[T, Unit]): UIO[SlackResponse[T]] =
    makeCall(request.toRequest(baseUrl))

  def apiCall[T, A](request: Request[T, A])(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[A], SlackResponse[T]] =
    ZIO.service[A].flatMap(auth => makeCall(ev(request.toRequest(baseUrl))(auth)))
}

object HttpSlack {
  final val SlackBaseUrl = "https://slack.com/api/"

  def make: ZIO[Has[SttpClient.Service], Nothing, SlackClient] = make(SlackBaseUrl)

  def make(url: String): ZIO[Has[SttpClient.Service], Nothing, SlackClient] =
    ZIO.service[SttpClient.Service].map(client => new HttpSlack(url, client))

  def layer: ZLayer[Has[Service], Nothing, Has[SlackClient]] = make.toLayer
}
