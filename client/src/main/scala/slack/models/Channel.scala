package slack.models

import io.circe.Json

case class Channel(
  id: String,
  name: String,
  created: Long,
  creator: Option[String],
  is_archived: Option[Boolean],
  is_member: Option[Boolean],
  is_general: Option[Boolean],
  is_channel: Option[Boolean],
  is_group: Option[Boolean],
  is_mpim: Option[Boolean],
  num_members: Option[Int],
  members: Option[Seq[String]],
  topic: Option[ChannelValue],
  purpose: Option[ChannelValue],
  last_read: Option[String],
  latest: Option[Json],
  unread_count: Option[Int],
  unread_count_display: Option[Int]
)

case class ChannelValue(value: String, creator: String, last_set: Long)
