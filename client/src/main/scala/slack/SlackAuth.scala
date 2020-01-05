package slack

import slack.models.AuthIdentity
import zio.ZIO

trait SlackAuth {
  val slackAuth: SlackAuth.Service[Any]
}

object SlackAuth {
  trait Service[R] {
    def test: ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("api.test")) >>= SlackExtractors.isOk

    def testAuth: ZIO[R with SlackEnv, SlackError, AuthIdentity] =
      sendM(request("auth.test")) >>= as[AuthIdentity]
  }
}

object auth extends SlackAuth.Service[SlackEnv]
