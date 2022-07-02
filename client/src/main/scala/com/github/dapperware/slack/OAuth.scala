package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedOauth
import com.github.dapperware.slack.generated.requests.{ AccessOauthRequest, AccessV2OauthRequest }
import com.github.dapperware.slack.generated.responses.AccessV2OauthResponse
import zio.{ Has, URIO }

trait OAuth { self: Slack =>

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  ): URIO[Has[ClientSecret], SlackResponse[AccessV2OauthResponse]] =
    clientApiCall(OAuth.accessV2Oauth(AccessV2OauthRequest(code, redirectUri)))

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  ): URIO[Has[ClientSecret], SlackResponse[Unit]] =
    clientApiCall(
      OAuth
        .accessOauth(AccessOauthRequest(code = code, redirect_uri = redirectUri, single_channel = singleChannel))
    )

  def exchangeV2(
    clientId: String,
    clientSecret: String
  ): URIO[Has[ClientSecret], SlackResponse[AccessV2OauthResponse]] =
    clientApiCall(
      request("oauth.v2.exchange")
        .formBody("client_id" -> clientId, "client_secret" -> clientSecret)
        .auth
        .clientSecret
        .as[AccessV2OauthResponse]
    )

}

trait OAuthAccessors {

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  ): URIO[Has[Slack] with Has[ClientSecret], SlackResponse[AccessV2OauthResponse]] =
    URIO.accessM(_.get.accessOAuthV2(code, redirectUri))

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[ClientSecret], SlackResponse[Unit]] =
    URIO.accessM(_.get.accessOAuth(code, redirectUri, singleChannel))

  def exchangeV2(
    clientId: String,
    clientSecret: String
  ): URIO[Has[Slack] with Has[ClientSecret], SlackResponse[AccessV2OauthResponse]] =
    URIO.accessM(_.get.exchangeV2(clientId, clientSecret))

}

object OAuth extends GeneratedOauth
