package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedBots
import com.github.dapperware.slack.generated.requests.InfoBotsRequest
import com.github.dapperware.slack.generated.responses.InfoBotsResponse
import zio.{ URIO, ZIO }

trait Bots { self: Slack =>
  def botsInfo(req: InfoBotsRequest): URIO[AccessToken, SlackResponse[InfoBotsResponse]] =
    apiCall(Bots.infoBots(req))
}

private[slack] trait BotsAccessors { self: Slack.type =>
  def botsInfo(req: InfoBotsRequest): URIO[Slack with AccessToken, SlackResponse[InfoBotsResponse]] =
    ZIO.serviceWithZIO[Slack](_.botsInfo(req))
}

object Bots extends GeneratedBots
