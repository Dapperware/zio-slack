package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class Message(
  user: String,
  text: String,
  thread_ts: String,
  ts: String,
  reply_count: Option[Int] = None,
  replies: Option[List[Reply]] = None,
  parent_user_id: Option[String] = None,
  unread_count: Option[Int] = None,
  subscribed: Option[Boolean] = None,
  last_read: Option[String] = None,
  `type`: String = "message"
)

object Message {
  implicit val codec: Codec[Message] = deriveCodec[Message]
}
