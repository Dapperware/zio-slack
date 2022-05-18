/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class RevokeAuthResponse(revoked: Boolean)

object RevokeAuthResponse {
  implicit val decoder: io.circe.Decoder[RevokeAuthResponse] =
    io.circe.generic.semiauto.deriveDecoder[RevokeAuthResponse]
}

case class TestAuthResponse(
  bot_id: Option[String] = None,
  is_enterprise_install: Option[Boolean] = None,
  team: String,
  team_id: String,
  url: String,
  user: String,
  user_id: String
)

object TestAuthResponse {
  implicit val decoder: io.circe.Decoder[TestAuthResponse] = io.circe.generic.semiauto.deriveDecoder[TestAuthResponse]
}
