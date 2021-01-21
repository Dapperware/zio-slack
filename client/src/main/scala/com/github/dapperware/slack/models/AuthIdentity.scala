package com.github.dapperware.slack.models

import io.circe.Decoder

case class AuthIdentity(url: String, team: String, user: String, team_id: String, user_id: String)

object AuthIdentity {
  implicit val decoder: Decoder[AuthIdentity] = io.circe.generic.semiauto.deriveDecoder[AuthIdentity]
}
