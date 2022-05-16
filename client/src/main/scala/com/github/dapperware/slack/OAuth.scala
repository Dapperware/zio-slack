package com.github.dapperware.slack

import com.github.dapperware.slack
import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.api.FullAccessTokenV2

trait OAuth {

  def accessOAuthV2(
    code: String,
    redirectUri: Option[String] = None
  ) =
    request("oauth.v2.access")
      .auth[ClientSecret]
      .as[FullAccessTokenV2]
      .formBody(
        "code"         -> code,
        "redirect_uri" -> redirectUri
      )

  def accessOAuth(
    code: String,
    redirectUri: Option[String] = None,
    singleChannel: Option[Boolean] = None
  ) =
    request("oauth.access")
      .auth[ClientSecret]
      .as[FullAccessTokenV2]
      .formBody(
        "code"           -> code,
        "redirect_uri"   -> redirectUri,
        "single_channel" -> singleChannel
      )

  def exchangeV2(
    clientId: String,
    clientSecret: String
  ) =
    request("oauth.v2.exchange")
      .formBody("client_id" -> clientId, "client_secret" -> clientSecret)
      .auth[ClientSecret]
      .as[FullAccessTokenV2]

}
