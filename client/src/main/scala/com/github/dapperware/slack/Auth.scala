package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedAuth
import com.github.dapperware.slack.generated.requests.RevokeAuthRequest
import com.github.dapperware.slack.generated.responses.{ RevokeAuthResponse, TestAuthResponse }
import zio.{ Has, URIO }

trait Auth { self: Slack =>
  def testAuth: URIO[Has[AccessToken], SlackResponse[TestAuthResponse]] =
    apiCall(Auth.testAuth)

  def revokeAuth(test: Option[Boolean]): URIO[Has[AccessToken], SlackResponse[RevokeAuthResponse]] =
    apiCall(Auth.revokeAuth(RevokeAuthRequest(test)))

}

private[slack] trait AuthAccessors { _: Slack.type =>
  def testAuth: URIO[Has[Slack] with Has[AccessToken], SlackResponse[TestAuthResponse]] =
    URIO.accessM(_.get.testAuth)

  def revokeAuth(test: Option[Boolean]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[RevokeAuthResponse]] =
    URIO.accessM(_.get.revokeAuth(test))
}

object Auth extends GeneratedAuth
