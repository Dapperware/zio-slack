package basic

import common.EnrichedManaged
import pureconfig._
import pureconfig.error.ConfigReaderException
import pureconfig.generic.semiauto._
import slack.api.{ realtime, web }
import slack.realtime.models.{ SendMessage, UserTyping }
import slack.realtime.{ SlackRealtimeClient, SlackRealtimeEnv }
import slack.{ AccessToken, SlackClient, SlackEnv, SlackError }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.macros.delegate.enrichWithM
import zio.macros.delegate.syntax._
import zio.stream.ZStream
import zio.{ ManagedApp, ZEnv, ZIO, ZManaged }

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val configReader: ConfigReader[BasicConfig] = deriveReader[BasicConfig]
}

object BasicApp extends ManagedApp with EnrichedManaged {
  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      backend <- AsyncHttpClientZioBackend().toManaged(_.close.ignore)
      source  = ConfigSource.defaultApplication
      config <- ZManaged
                 .fromEither(source.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      client       = SlackClient.make(backend)
      withRealtime = SlackRealtimeClient.make(backend)
      accessToken  = AccessToken.make(config.token)
      env          = ZManaged.environment[ZEnv] @@ enrichWithM(client) @@ enrichWithM(accessToken) @@ enrichWithM(withRealtime)
      resp         <- testApi(config).provideSomeManaged(env).toManaged_
      _            <- testRealtime(config).provideSomeManaged(env)
    } yield resp).either.flatMap {
      case Left(value)  => putStrLn(value.getMessage) as 1 toManaged_
      case Right(value) => putStrLn(value) as 0 toManaged_
    }

  def testRealtime(config: BasicConfig): ZManaged[SlackRealtimeEnv with Console, SlackError, Unit] =
    for {
      // Test that we can receive messages
      receiver <- realtime.connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
      _ <- receiver.collectM {
            case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel")
            case _                         => ZIO.unit
          }.runDrain.toManaged_
    } yield ()

  def testApi(config: BasicConfig): ZIO[SlackEnv, SlackError, String] =
    for {
      resp <- web.postChatMessage(config.channel, text = "Hello Slack client!")
      _    <- web.addReactionToMessage("+1", config.channel, resp)
    } yield resp

}
