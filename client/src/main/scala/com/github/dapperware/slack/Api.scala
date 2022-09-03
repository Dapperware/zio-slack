package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedApi
import com.github.dapperware.slack.generated.requests.TestApiRequest
import zio.{ Trace, URIO, ZIO }

trait Api { self: SlackApiBase =>
  def testApi(error: Option[String])(implicit trace: Trace): URIO[NoAuth, SlackResponse[Unit]] =
    apiCall(Api.testApi(TestApiRequest(error, None)))
}

trait ApiAccessors { self: Slack.type =>

  def testApi(error: Option[String])(implicit trace: Trace): ZIO[NoAuth with Slack, Nothing, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.testApi(error))

}

object Api extends GeneratedApi
