package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedBots
import com.github.dapperware.slack.generated.requests.InfoBotsRequest
import com.github.dapperware.slack.generated.responses.InfoBotsResponse
import zio.{ Has, URIO }

trait Bots { self: Slack =>
  def botsInfo(req: InfoBotsRequest): URIO[Has[AccessToken], SlackResponse[InfoBotsResponse]] =
    apiCall(Bots.infoBots(req))
}

private[slack] trait BotsAccessors { _: Slack.type =>
  def botsInfo(req: InfoBotsRequest): URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoBotsResponse]] =
    URIO.accessM(_.get.botsInfo(req))
}

object Bots extends GeneratedBots
