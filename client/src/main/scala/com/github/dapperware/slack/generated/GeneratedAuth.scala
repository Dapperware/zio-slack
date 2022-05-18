/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedAuth {

  /**
   * Revokes a token.
   * @see https://api.slack.com/methods/auth.revoke
   */
  def revokeAuth(req: RevokeAuthRequest): Request[RevokeAuthResponse, AccessToken] =
    request("auth.revoke").formBody(req).as[RevokeAuthResponse].auth.accessToken

  /**
   * Checks authentication & identity.
   * @see https://api.slack.com/methods/auth.test
   */
  def testAuth: Request[TestAuthResponse, AccessToken] =
    request("auth.test").as[TestAuthResponse].auth.accessToken

}
