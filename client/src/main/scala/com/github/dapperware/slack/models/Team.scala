package com.github.dapperware.slack.models

import io.circe.generic.semiauto.deriveDecoder
import io.circe.{ Decoder, Json }

case class Team(
  id: String,
  name: String,
  domain: String,
  email_domain: String,
  msg_edit_window_mins: Int,
  over_storage_limit: Boolean,
  prefs: Json,
  plan: String
)

object Team {
  implicit val decoder: Decoder[Team] = deriveDecoder[Team]
}
