package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedOauth
import com.github.dapperware.slack.generated.requests.{ AccessOauthRequest, AccessV2OauthRequest }
import com.github.dapperware.slack.generated.responses.AccessV2OauthResponse
import zio.{ Trace, URIO, ZIO }

trait OAuth { self: SlackApiBase =>

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  )(implicit trace: Trace): URIO[ClientSecret, SlackResponse[AccessV2OauthResponse]] =
    apiCall(OAuth.accessV2Oauth(AccessV2OauthRequest(code, redirectUri)))

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  )(implicit trace: Trace): URIO[ClientSecret, SlackResponse[Unit]] =
    apiCall(
      OAuth
        .accessOauth(AccessOauthRequest(code = code, redirect_uri = redirectUri, single_channel = singleChannel))
    )

  def exchangeV2(
    clientId: String,
    clientSecret: String
  )(implicit trace: Trace): URIO[ClientSecret, SlackResponse[AccessV2OauthResponse]] =
    apiCall(
      request("oauth.v2.exchange")
        .formBody("client_id" -> clientId, "client_secret" -> clientSecret)
        .auth
        .clientSecret
        .as[AccessV2OauthResponse]
    )

}

trait OAuthAccessors { self: Slack.type =>

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with ClientSecret, SlackResponse[AccessV2OauthResponse]] =
    ZIO.serviceWithZIO[Slack](_.accessOAuthV2(code, redirectUri))

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  )(implicit trace: Trace): URIO[Slack with ClientSecret, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.accessOAuth(code, redirectUri, singleChannel))

  def exchangeV2(
    clientId: String,
    clientSecret: String
  )(implicit trace: Trace): URIO[Slack with ClientSecret, SlackResponse[AccessV2OauthResponse]] =
    ZIO.serviceWithZIO[Slack](_.exchangeV2(clientId, clientSecret))

}

object OAuth extends GeneratedOauth
