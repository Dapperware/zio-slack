package basic

import com.github.dapperware.slack.models.{ SendMessage, UserTyping }
import com.github.dapperware.slack.{ AccessToken, Slack, SlackError, SlackSocket, SlackSocketLive }
import common.{ botToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.magic._
import zio.stream.ZStream
import zio.{ App, ExitCode, Has, ZIO, ZLayer, ZManaged }

object BasicApp extends App {

  val layers: ZLayer[Any, Throwable, Has[Slack] with Has[SlackSocket] with Has[AccessToken] with Has[BasicConfig]] =
    ZLayer.fromMagic[Has[Slack] with Has[SlackSocket] with Has[AccessToken] with Has[BasicConfig]](
      AsyncHttpClientZioBackend.layer(),
      Slack.http,
      SlackSocketLive.layer,
      botToken.toLayer,
      default
    )

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[Has[Slack] with Has[AccessToken] with Has[SlackSocket] with Has[
    BasicConfig
  ] with Console, SlackError, Unit] =
    for {
      config <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      _      <- SlackSocket
                  .connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
                  .collectM {
                    case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel").orDie
                    case _                         => ZIO.unit
                  }
                  .runDrain
                  .toManaged_
    } yield ()

  val testApi: ZIO[Has[Slack] with Has[AccessToken] with Has[BasicConfig], SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- Slack.postChatMessage(config.channel, text = Some("Hello Slack client!")).map(_.toEither).absolve
      _      <- Slack.addReactionToMessage("+1", config.channel, resp.ts)
    } yield resp.ts

}
