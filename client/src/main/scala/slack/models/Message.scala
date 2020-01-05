package slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class Message(
  user: String,
  text: String,
  threadTs: String,
  replyCount: Option[Int],
  replies: Option[List[Reply]],
  parentUserId: Option[String],
  ts: String,
  unreadCount: Option[Int],
  subscribed: Option[Boolean],
  lastRead: Option[String],
  `type`: String = "message"
)

object Message {
  implicit val codec: Codec[Message] = deriveCodec[Message]
}
