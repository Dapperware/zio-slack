package basic

import pureconfig._
import pureconfig.error.ConfigReaderException
import pureconfig.generic.semiauto._
import slack.api.{ realtime, web }
import slack.client.SlackClient
import slack.realtime.models.{ SendMessage, UserTyping }
import slack.realtime.{ SlackRealtimeClient, SlackRealtimeEnv }
import slack.{ AccessToken, SlackEnv, SlackError }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.stream.ZStream
import zio.{ App, ExitCode, ZIO, ZManaged }

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val configReader: ConfigReader[BasicConfig] = deriveReader[BasicConfig]
}

object BasicApp extends App {
  val layers = AsyncHttpClientZioBackend.layer() >>>
    (SlackClient.live ++ SlackRealtimeClient.live)

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      config <- ZManaged
                 .fromEither(ConfigSource.defaultApplication.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      env  = layers ++ AccessToken.make(config.token).toLayer
      resp <- testApi(config).provideLayer(env).toManaged_
      _    <- testRealtime(config).provideSomeLayer[Console](env)
    } yield resp).use_(ZIO.unit).exitCode

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
