package com.github.dapperware.slack.api

import com.github.dapperware.slack.models.Bot
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackBot {
  trait Service {
    def botsInfo(bot: Option[String]): ZIO[SlackEnv, SlackError, Bot] =
      sendM(request("bots.info", "bot" -> bot)) >>= as[Bot]
  }
}
