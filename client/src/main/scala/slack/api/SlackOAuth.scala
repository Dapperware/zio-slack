package slack.api

import io.circe
import io.circe.Decoder
import io.circe.generic.semiauto._
import slack.SlackError
import slack.core.{ ClientSecret, SlackClient }
import sttp.client.Request
import zio.{ URIO, ZIO }

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
      sendRaw(
        request(
          "oauth.access",
          "code"           -> code,
          "redirect_uri"   -> redirectUri,
          "single_channel" -> singleChannel
        )
      ) >>= as[FullAccessToken]

    def sendRaw[T](
      request: URIO[R, Request[SlackResponse[T], Nothing]]
    ): ZIO[R with SlackClient with ClientSecret, Throwable, T] =
      request >>= ClientSecret.authenticateM >>= SlackClient.send[T, circe.Error]
  }
}

object oauth extends SlackOAuth.Service[SlackClient with ClientSecret]

case class FullAccessToken(access_token: String,
                           scope: String,
                           team_name: String,
                           team_id: String,
                           enterprise_id: Option[String])

object FullAccessToken {
  implicit val decoder: Decoder[FullAccessToken] = deriveDecoder[FullAccessToken]
}
