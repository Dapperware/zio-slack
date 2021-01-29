package com.github.dapperware.slack.api

import com.github.dapperware.slack.models.AuthIdentity
import com.github.dapperware.slack.{ SlackEnv, SlackError, SlackExtractors }
import zio.ZIO

trait SlackAuth {
  def test: ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("api.test")) >>= SlackExtractors.isOk

  def testAuth: ZIO[SlackEnv, SlackError, AuthIdentity] =
    sendM(request("auth.test")) >>= as[AuthIdentity]
}

object auth extends SlackAuth
