package com.github.dapperware.slack

import io.circe.Decoder
import zio.{Has, URIO}

case class EmojiMap(
  emoji: Map[String, String]
)

object EmojiMap {
  implicit val decoder: Decoder[EmojiMap] =
    Decoder.forProduct1("emoji")(EmojiMap.apply)
}

trait Emojis {
  def listEmojis: URIO[Has[Slack] with Has[AccessToken], SlackResponse[EmojiMap]] =
    Slack.apiCall(
      Request
        .make(
          "emoji.list",
          SlackBody.fromForm(Map.empty[String, String])
        )
        .as[EmojiMap]
    )
}
