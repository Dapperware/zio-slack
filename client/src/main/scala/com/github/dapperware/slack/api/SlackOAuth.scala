package com.github.dapperware.slack.api

import com.github.dapperware.slack.client.send
import com.github.dapperware.slack.{ ClientSecret, SlackClient, SlackError }
import io.circe
import io.circe.generic.semiauto._
import io.circe.{ Decoder, Json }
import sttp.client3.Request
import zio.{ Has, URIO, ZIO }

trait SlackOAuth {

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  ): ZIO[Has[SlackClient] with Has[ClientSecret], SlackError, FullAccessTokenV2] =
    request("oauth.v2.access", "code" -> code, "redirect_uri" -> redirectUri) >>= ClientSecret.authenticateM >>=
      send[Json, circe.Error] >>= as[FullAccessTokenV2]

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  ): ZIO[Has[SlackClient] with Has[ClientSecret], SlackError, FullAccessToken] =
    sendRaw(
      request(
        "oauth.access",
        "code"           -> code,
        "redirect_uri"   -> redirectUri,
        "single_channel" -> singleChannel
      )
    ) >>= as[FullAccessToken]

  protected def sendRaw[T](
    request: URIO[Any, Request[SlackResponse[T], Any]]
  ): ZIO[Has[SlackClient] with Has[ClientSecret], Throwable, T] =
    request >>= ClientSecret.authenticateM >>= send[T, circe.Error]
}

object oauth extends SlackOAuth

case class BotAccessToken(
  bot_user_id: String,
  bot_access_token: String
)

object BotAccessToken {
  implicit val decoder: Decoder[BotAccessToken] = deriveDecoder[BotAccessToken]
}

case class FullAccessToken(
  access_token: String,
  scope: String,
  team_name: String,
  team_id: String,
  enterprise_id: Option[String],
  bot: Option[BotAccessToken]
)

object FullAccessToken {
  implicit val decoder: Decoder[FullAccessToken] = deriveDecoder[FullAccessToken]
}

case class FullAccessTokenV2(
  access_token: Option[String],
  token_type: Option[String],
  scope: Option[String],
  bot_user_id: Option[String],
  app_id: String,
  team: MinimalTeam,
  enterprise: Option[MinimalTeam],
  authed_user: Option[AuthedUser]
)

case class MinimalTeam(
  id: String,
  name: String
)

object MinimalTeam {
  implicit val decoder: Decoder[MinimalTeam] = deriveDecoder[MinimalTeam]
}

case class AuthedUser(
  id: String,
  scope: Option[String],
  access_token: Option[String],
  token_type: Option[String]
)

object AuthedUser {
  implicit val decoder: Decoder[AuthedUser] = deriveDecoder[AuthedUser]
}

object FullAccessTokenV2 {
  implicit val decoder: Decoder[FullAccessTokenV2] = deriveDecoder[FullAccessTokenV2]
}
