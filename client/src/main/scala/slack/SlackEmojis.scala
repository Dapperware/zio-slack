package slack

import zio.ZIO
import zio.macros.annotation.mockable

@mockable
trait SlackEmojis {
  val slackEmojis: SlackEmojis.Service[Any]
}

object SlackEmojis {
  trait Service[R] {
    def listEmojis: ZIO[R with SlackEnv, SlackError, Map[String, String]] =
      sendM(request("emoji.list")) >>= as[Map[String, String]]("emoji")
  }

}

object emojis extends SlackEmojis.Service[SlackEnv]
