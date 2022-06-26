/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class AccessV2OauthResponse(
  access_token: Option[String] = None,
  scope: Option[String] = None,
  expires_in: Option[Int] = None,
  token_type: Option[String] = None,
  bot_user_id: Option[String] = None,
  app_id: Option[String] = None,
  authed_user: Option[AuthedUser] = None,
  team: Option[AuthedTeam] = None,
  enterprise: Option[AuthedEnterprise] = None,
  refresh_token: Option[String] = None,
  is_enterprise_install: Option[Boolean] = None
)

object AccessV2OauthResponse {
  implicit val decoder: io.circe.Decoder[AccessV2OauthResponse] =
    io.circe.generic.semiauto.deriveDecoder[AccessV2OauthResponse]
}
