/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedOauth {

  /**
   * Exchanges a temporary OAuth verifier code for an access token.
   * @see https://api.slack.com/methods/oauth.access
   */
  def accessOauth(req: AccessOauthRequest): Request[Unit, ClientSecret] =
    request("oauth.access").formBody(req).auth.clientSecret

  /**
   * Exchanges a temporary OAuth verifier code for a workspace token.
   * @see https://api.slack.com/methods/oauth.token
   */
  def tokenOauth(req: TokenOauthRequest): Request[Unit, ClientSecret] =
    request("oauth.token").formBody(req).auth.clientSecret

  /**
   * Exchanges a temporary OAuth verifier code for an access token.
   * @see https://api.slack.com/methods/oauth.v2.access
   */
  def accessV2Oauth(req: AccessV2OauthRequest): Request[Unit, ClientSecret] =
    request("oauth.v2.access").formBody(req).auth.clientSecret

}
