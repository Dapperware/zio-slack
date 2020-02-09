package slack.api

import slack.{ SlackEnv, SlackError }
import zio.ZIO

//@mockable
trait SlackEmojis {
  val slackEmojis: SlackEmojis.Service
}

object SlackEmojis {
  trait Service {
    def listEmojis: ZIO[SlackEnv, SlackError, Map[String, String]] =
      sendM(request("emoji.list")) >>= as[Map[String, String]]("emoji")
  }

}

object emojis extends SlackEmojis.Service
