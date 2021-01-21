package com.github.dapperware.slack.models

import io.circe.Json

case class Channel(
  id: String,
  name: String,
  created: Long,
  creator: Option[String] = None,
  is_archived: Option[Boolean] = None,
  is_member: Option[Boolean] = None,
  is_general: Option[Boolean] = None,
  is_channel: Option[Boolean] = None,
  is_group: Option[Boolean] = None,
  is_mpim: Option[Boolean] = None,
  num_members: Option[Int] = None,
  members: Option[Seq[String]] = None,
  topic: Option[ChannelValue] = None,
  purpose: Option[ChannelValue] = None,
  last_read: Option[String] = None,
  latest: Option[Json] = None,
  unread_count: Option[Int] = None,
  unread_count_display: Option[Int] = None
)

case class ChannelValue(value: String, creator: String, last_set: Long)
