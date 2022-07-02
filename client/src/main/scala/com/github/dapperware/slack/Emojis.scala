package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedEmoji
import io.circe.Decoder
import zio.{ Has, URIO, ZIO }

case class EmojiMap(
  emoji: Map[String, String]
)

object EmojiMap {
  implicit val decoder: Decoder[EmojiMap] =
    Decoder.forProduct1("emoji")(EmojiMap.apply)
}

trait Emojis { self: Slack =>
  def listEmojis: URIO[Has[AccessToken], SlackResponse[EmojiMap]] =
    apiCall(
      request("emoji.list")
        .formBody(Map.empty[String, String])
        .as[EmojiMap]
    )
}

private[slack] trait EmojisAccessors { _: Slack.type =>
  def listEmojis: URIO[Has[Slack] with Has[AccessToken], SlackResponse[EmojiMap]] =
    ZIO.accessM(_.get.listEmojis)

}

object Emojis extends GeneratedEmoji
