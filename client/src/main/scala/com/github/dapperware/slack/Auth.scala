package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedAuthRequest
import com.github.dapperware.slack.generated.GeneratedAuth
import com.github.dapperware.slack.generated.requests.RevokeAuthRequest
import com.github.dapperware.slack.generated.responses.{ RevokeAuthResponse, TestAuthResponse }
import zio.{ Has, URIO }

trait Auth {
  def testAuth: URIO[Has[Slack] with Has[AccessToken], SlackResponse[TestAuthResponse]] =
    Auth.testAuth.toCall

  def revokeAuth(req: RevokeAuthRequest): URIO[Has[Slack] with Has[AccessToken], SlackResponse[RevokeAuthResponse]] =
    Auth.revokeAuth(req).toCall

}

object Auth extends GeneratedAuth
