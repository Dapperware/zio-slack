package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedAuthRequest
import com.github.dapperware.slack.generated.GeneratedBots
import com.github.dapperware.slack.generated.requests.InfoBotsRequest
import com.github.dapperware.slack.generated.responses.InfoBotsResponse
import zio.{ Has, URIO }

trait Bots {
  def botsInfo(req: InfoBotsRequest): URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoBotsResponse]] =
    Bots.infoBots(req).toCall
}

object Bots extends GeneratedBots
