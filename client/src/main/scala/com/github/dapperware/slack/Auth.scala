package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedRequest }
import com.github.dapperware.slack.models.AuthIdentity
import zio.{ Has, URIO }

trait Auth {
  def test: URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("api.test").toCall

  def testAuth: URIO[Has[Slack] with Has[AccessToken], SlackResponse[AuthIdentity]] =
    request("auth.test").as[AuthIdentity].toCall

}
