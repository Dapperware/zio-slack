package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class Message(
  user: Option[String] = None,
  text: Option[String] = None,
  thread_ts: Option[String] = None,
  ts: Option[String] = None,
  team: Option[String] = None,
  hidden: Option[Boolean] = None,
  deleted_ts: Option[String] = None,
  event_ts: Option[String] = None,
  bot_id: Option[String] = None,
  username: Option[String] = None,
  icons: Option[Icon] = None,
  bot_profile: Option[BotProfile] = None,
  inviter: Option[String] = None,
  topic: Option[String] = None,
  purpose: Option[String] = None,
  name: Option[String] = None,
  old_name: Option[String] = None,
  members: Option[String] = None,
  parent_user_id: Option[String] = None,
  files: Option[List[SlackFile]] = None,
  upload: Option[Boolean] = None,
  comment: Option[SlackComment] = None,
  item_type: Option[String] = None,
  reply_count: Option[Int] = None,
  replies: Option[List[Reply]] = None,
  reply_to: Option[Int] = None,
  unread_count: Option[Int] = None,
  subscribed: Option[Boolean] = None,
  last_read: Option[String] = None,
  blocks: Option[Seq[Block]] = None,
  attachments: Option[List[Attachment]] = None,
  reactions: Option[List[Reaction]] = None,
  response_type: Option[String] = None,
  delete_original: Option[Boolean] = None,
  replace_original: Option[Boolean] = None,
  `type`: String = "message"
)

object Message {
  implicit val codec: Codec[Message] = deriveCodec[Message]
}

final case class Icon(icon_url: Option[String] = None, icon_emoji: Option[String] = None)
object Icon       {
  implicit val codec: Codec[Icon] = deriveCodec[Icon]
}
final case class BotProfile(
  app_id: Option[String] = None,
  deleted: Option[Boolean] = None,
  icons: Option[Icons] = None,
  id: Option[String] = None,
  name: Option[String] = None,
  team_id: Option[String] = None,
  updated: Option[Long] = None
)
object BotProfile {
  implicit val codec: Codec[BotProfile] = deriveCodec[BotProfile]
}
final case class Icons(
  image_36: Option[String] = None,
  image_48: Option[String] = None,
  image_72: Option[String] = None
)
object Icons      {
  implicit val codec: Codec[Icons] = deriveCodec[Icons]
}
