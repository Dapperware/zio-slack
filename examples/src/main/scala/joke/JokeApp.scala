package joke

import basic.BasicConfig
import io.circe
import io.circe.{ DecodingFailure, Json }
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.api.chats._
import slack.core.{ AccessToken, SlackClient }
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio.clock.Clock
import zio.duration._
import zio.{ IO, ManagedApp, Schedule, ZManaged }

object JokeApp extends ManagedApp {

  val getJoke: Request[Either[ResponseError[circe.Error], Either[DecodingFailure, String]], Nothing] = basicRequest
    .get(uri"https://api.chucknorris.io/jokes/random")
    .response(asJson[Json])
    .mapResponseRight(_.hcursor.downField("value").as[String])

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      backend <- AsyncHttpClientZioBackend().toManaged(_.close.orDie)
      source  = ConfigSource.defaultApplication
      config <- ZManaged
                 .fromEither(source.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      token  <- AccessToken.makeManaged(config.token)
      client <- SlackClient.makeManaged(backend)
      _ <- (for {
            resp <- backend.send(getJoke)
            body <- IO.fromEither(resp.body)
            joke <- IO.fromEither(body)
            _    <- postChatMessage(config.channel, joke)
          } yield ())
            .repeat(Schedule.fixed(3.hours))
            .toManaged_
            .provideSome[Clock](
              c =>
                new Clock with SlackClient with AccessToken {
                  override val slackClient: SlackClient.Service = client.slackClient
                  override val accessToken: AccessToken.Service = token.accessToken
                  override val clock: Clock.Service[Any]        = c.clock
              }
            )
    } yield ()).fold(ex => 1, _ => 0)
}
