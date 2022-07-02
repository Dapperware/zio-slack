package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

import java.time.Instant

case class UserGroup(
  id: String,
  team_id: String,
  is_usergroup: Boolean,
  is_external: Boolean,
  name: String,
  description: String,
  handle: String,
  date_create: Instant,
  prefs: UserPrefs,
  date_update: Option[Instant],
  date_delete: Option[Instant],
  deleted_by: Option[String],
  created_by: Option[String],
  updated_by: Option[String],
  auto_type: Option[String],
  users: Option[List[String]],
  user_count: Option[Int], // TODO map to Int
  auto_provision: Option[Boolean],
  channel_count: Option[Int],
  enterprise_subteam_id: Option[String]
)

object UserGroup {

  implicit val decoder: Decoder[UserGroup] =
    deriveDecoder[UserGroup]
}

case class UserPrefs(
  channels: List[String],
  groups: List[String]
)

object UserPrefs {
  implicit val prefsDecoder: Decoder[UserPrefs] =
    deriveDecoder[UserPrefs]
}
