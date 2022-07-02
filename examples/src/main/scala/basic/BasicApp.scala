package basic

import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.realtime.models.{ SendMessage, UserTyping }
import com.github.dapperware.slack.{ AccessToken, HttpSlack, Slack, SlackError }
import common.{ accessToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.stream.ZStream
import zio.{ App, ExitCode, Has, ZIO, ZLayer, ZManaged }
import zio.magic._

object BasicApp extends App {

  val layers
    : ZLayer[Any, Throwable, Has[Slack] with Has[SlackRealtimeClient] with Has[AccessToken] with Has[BasicConfig]] =
    ZLayer.fromMagic[Has[Slack] with Has[SlackRealtimeClient] with Has[AccessToken] with Has[BasicConfig]](
      AsyncHttpClientZioBackend.layer(),
      HttpSlack.layer,
      SlackRealtimeClient.live,
      accessToken.toLayer,
      default
    )

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[Has[Slack] with Has[AccessToken] with Has[SlackRealtimeClient] with Has[
    BasicConfig
  ] with Console, SlackError, Unit] =
    for {
      config   <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      receiver <- SlackRealtimeClient.connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
      _        <- receiver.collectM {
                    case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel").orDie
                    case _                         => ZIO.unit
                  }.runDrain.toManaged_
    } yield ()

  val testApi: ZIO[Has[Slack] with Has[AccessToken] with Has[BasicConfig], SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- Slack.postChatMessage(config.channel, text = Some("Hello Slack client!")).map(_.toEither).absolve
      _      <- Slack.addReactionToMessage("+1", config.channel, resp.ts)
    } yield resp.ts

}
