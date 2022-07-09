package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedAuth
import com.github.dapperware.slack.generated.requests.RevokeAuthRequest
import com.github.dapperware.slack.generated.responses.{ RevokeAuthResponse, TestAuthResponse }
import zio.{ URIO, ZIO }

trait Auth { self: Slack =>
  def testAuth: URIO[AccessToken, SlackResponse[TestAuthResponse]] =
    apiCall(Auth.testAuth)

  def revokeAuth(test: Option[Boolean]): URIO[AccessToken, SlackResponse[RevokeAuthResponse]] =
    apiCall(Auth.revokeAuth(RevokeAuthRequest(test)))

}

private[slack] trait AuthAccessors { self: Slack.type =>
  def testAuth: URIO[Slack with AccessToken, SlackResponse[TestAuthResponse]] =
    ZIO.serviceWithZIO[Slack](_.testAuth)

  def revokeAuth(test: Option[Boolean]): URIO[Slack with AccessToken, SlackResponse[RevokeAuthResponse]] =
    ZIO.serviceWithZIO[Slack](_.revokeAuth(test))
}

object Auth extends GeneratedAuth
