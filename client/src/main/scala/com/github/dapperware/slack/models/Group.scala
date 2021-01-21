package com.github.dapperware.slack.models

import io.circe.Json

case class Group(id: String,
                 name: String,
                 is_group: Boolean,
                 created: Long,
                 creator: String,
                 is_archived: Boolean,
                 members: Seq[String],
                 topic: GroupValue,
                 purpose: GroupValue,
                 last_read: Option[String],
                 latest: Option[Json],
                 unread_count: Option[Int],
                 unread_count_display: Option[Int])

case class GroupValue(value: String, creator: String, last_set: Long)
