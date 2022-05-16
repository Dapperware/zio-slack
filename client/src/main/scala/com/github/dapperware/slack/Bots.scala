package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.Bot

trait Bots {
  def botsInfo(bot: Option[String]) =
    request("bots.info").formBody("bot" -> bot).as[Bot]
}
