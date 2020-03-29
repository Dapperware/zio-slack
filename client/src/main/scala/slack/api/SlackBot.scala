package slack.api

import slack.models.Bot
import slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackBot {
  trait Service {
    def botsInfo(bot: Option[String]): ZIO[SlackEnv, SlackError, Bot] =
      sendM(request("bots.info", "bot" -> bot)) >>= as[Bot]
  }
}
