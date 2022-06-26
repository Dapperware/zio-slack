package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Authorization(
  enterprise_id: Option[String],
  team_id: String,
  user_id: String,
  is_bot: Boolean,
  is_enterprise_install: Boolean
)

object Authorization {
  implicit val decoder: Decoder[Authorization] = deriveDecoder[Authorization]
}
