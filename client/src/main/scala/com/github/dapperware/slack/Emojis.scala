package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedEmoji
import io.circe.Decoder
import zio.{ URIO, ZIO }

case class EmojiMap(
  emoji: Map[String, String]
)

object EmojiMap {
  implicit val decoder: Decoder[EmojiMap] =
    Decoder.forProduct1("emoji")(EmojiMap.apply)
}

trait Emojis { self: SlackApiBase =>
  def listEmojis: URIO[AccessToken, SlackResponse[EmojiMap]] =
    apiCall(
      request("emoji.list")
        .formBody(Map.empty[String, String])
        .as[EmojiMap]
    )
}

private[slack] trait EmojisAccessors { self: Slack.type =>
  def listEmojis: URIO[Slack with AccessToken, SlackResponse[EmojiMap]] =
    ZIO.serviceWithZIO[Slack](_.listEmojis)

}

object Emojis extends GeneratedEmoji
