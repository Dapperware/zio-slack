package slack.api

import slack.SlackError
import slack.core.{ ClientSecret, SlackClient }
import zio.ZIO

trait SlackOAuth {
  val slackOAuth: SlackOAuth.Service[Any]
}

object SlackOAuth {
  trait Service[R] {
    def accessOAuth(
      code: String,
      redirectUri: Option[String] = None,
      singleChannel: Option[Boolean] = None
    ): ZIO[R with SlackClient with ClientSecret, SlackError, FullAccessToken] =

  }
}

case class FullAccessToken(access_token: String,
                           scope: String,
                           team_name: String,
                           team_id: String,
                           enterprise_id: Option[String])
