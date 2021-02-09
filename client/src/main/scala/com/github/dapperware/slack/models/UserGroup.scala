package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

import java.time.Instant

case class UserGroup(
  id: String,
  team_id: String,
  is_usergroup: Boolean,
  is_external: Option[Boolean],
  name: Option[String],
  description: Option[String],
  handle: Option[String],
  date_create: Instant,
  date_update: Option[Instant],
  date_delete: Option[Instant],
  deleted_by: Option[String],
  auto_type: Option[String],
  users: Option[List[String]],
  user_count: Option[String] // TODO map to Int
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
