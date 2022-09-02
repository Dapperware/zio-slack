package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedBots
import com.github.dapperware.slack.generated.requests.InfoBotsRequest
import com.github.dapperware.slack.generated.responses.InfoBotsResponse
import zio.{ Trace, URIO, ZIO }

trait Bots { self: SlackApiBase =>
  def botsInfo(req: InfoBotsRequest)(implicit trace: Trace): URIO[AccessToken, SlackResponse[InfoBotsResponse]] =
    apiCall(Bots.infoBots(req))
}

private[slack] trait BotsAccessors { self: Slack.type =>
  def botsInfo(req: InfoBotsRequest)(implicit
    trace: Trace
  ): URIO[Slack with AccessToken, SlackResponse[InfoBotsResponse]] =
    ZIO.serviceWithZIO[Slack](_.botsInfo(req))
}

object Bots extends GeneratedBots
