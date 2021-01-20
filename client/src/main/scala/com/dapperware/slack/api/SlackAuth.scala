package com.dapperware.slack.api

import com.dapperware.slack.models.AuthIdentity
import com.dapperware.slack.{SlackEnv, SlackError, SlackExtractors}
import zio.ZIO

trait SlackAuth {
  def test: ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("api.test")) >>= SlackExtractors.isOk

  def testAuth: ZIO[SlackEnv, SlackError, AuthIdentity] =
    sendM(request("auth.test")) >>= as[AuthIdentity]
}

object auth extends SlackAuth
