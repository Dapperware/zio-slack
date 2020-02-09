package slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class Message(
  user: String,
  text: String,
  thread_ts: String,
  reply_count: Option[Int],
  replies: Option[List[Reply]],
  parent_user_id: Option[String],
  ts: String,
  unread_count: Option[Int],
  subscribed: Option[Boolean],
  last_read: Option[String],
  `type`: String = "message"
)

object Message {
  implicit val codec: Codec[Message] = deriveCodec[Message]
}
