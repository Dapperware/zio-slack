package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedEmoji
import io.circe.Decoder
import zio.{ Has, URIO }

case class EmojiMap(
  emoji: Map[String, String]
)

object EmojiMap {
  implicit val decoder: Decoder[EmojiMap] =
    Decoder.forProduct1("emoji")(EmojiMap.apply)
}

trait Emojis {
  def listEmojis: URIO[Has[Slack] with Has[AccessToken], SlackResponse[EmojiMap]] =
    request("emoji.list")
      .formBody(Map.empty[String, String])
      .as[EmojiMap]
      .toCall

}

object Emojis extends GeneratedEmoji
