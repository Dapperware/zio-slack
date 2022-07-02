package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedOauth
import com.github.dapperware.slack.generated.requests.{ AccessOauthRequest, AccessV2OauthRequest }
import com.github.dapperware.slack.generated.responses.AccessV2OauthResponse
import zio.{ Has, URIO }

trait OAuth {

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  ): URIO[Has[Slack] with Has[ClientSecret], SlackResponse[AccessV2OauthResponse]] =
    OAuth.accessV2Oauth(AccessV2OauthRequest(code, redirectUri)).toCall

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  ) =
    OAuth
      .accessOauth(AccessOauthRequest(code = code, redirect_uri = redirectUri, single_channel = singleChannel))
      .toCall

  def exchangeV2(
    clientId: String,
    clientSecret: String
  ) =
    request("oauth.v2.exchange")
      .formBody("client_id" -> clientId, "client_secret" -> clientSecret)
      .auth
      .clientSecret
      .as[AccessV2OauthResponse]

}

object OAuth extends GeneratedOauth
