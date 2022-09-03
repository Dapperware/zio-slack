package com.github.dapperware.slack

import sttp.client3.{ Identity, RequestT, SttpBackend }
import zio.{ Tag, Task, Trace, UIO, URIO, ZIO, ZLayer }

class HttpSlack private (baseUrl: String, client: SttpBackend[Task, Any]) extends SlackClient {
  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[A, SlackResponse[T]] =
    ZIO.service[A].flatMap(auth => makeCall(request.toRequest(baseUrl, auth)))

  private def makeCall[A](
    request: RequestT[Identity, SlackResponse[A], Any]
  )(implicit trace: Trace): URIO[Any, SlackResponse[A]] =
    client
      .send(request)
      .mapBoth(SlackError.fromThrowable, _.body)
      .merge
}

object HttpSlack {
  final val SlackBaseUrl = "https://slack.com/api/"

  def make(implicit trace: Trace): ZIO[SttpBackend[Task, Any], Nothing, SlackClient] = make(SlackBaseUrl)

  def make(url: String)(implicit trace: Trace): ZIO[SttpBackend[Task, Any], Nothing, SlackClient] =
    ZIO.service[SttpBackend[Task, Any]].map(client => new HttpSlack(url, client))

  def layer(implicit trace: Trace): ZLayer[SttpBackend[Task, Any], Nothing, SlackClient] = ZLayer(make)
}
