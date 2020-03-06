package slack.api

import slack.models.AuthIdentity
import slack.{ SlackEnv, SlackError, SlackExtractors }
import zio.ZIO

object SlackAuth {
  trait Service {
    def test: ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("api.test")) >>= SlackExtractors.isOk

    def testAuth: ZIO[SlackEnv, SlackError, AuthIdentity] =
      sendM(request("auth.test")) >>= as[AuthIdentity]
  }
}

object auth extends SlackAuth.Service
