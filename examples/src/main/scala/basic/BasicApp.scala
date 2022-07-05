package basic

import com.github.dapperware.slack.models.events.SocketEventPayload.Event
import com.github.dapperware.slack.models.events.{ Hello, UserTyping }
import com.github.dapperware.slack.{ AccessToken, AppToken, Slack, SlackError, SlackSocket, SlackSocketLive }
import common.{ appToken, botToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.magic._
import zio.{ App, ExitCode, Has, ZIO, ZLayer, ZManaged }

object BasicApp extends App {

  val layers =
    ZLayer.fromMagic[Has[Slack] with Has[SlackSocket] with Has[AccessToken] with Has[BasicConfig] with Has[AppToken]](
      AsyncHttpClientZioBackend.layer(),
      Slack.http,
      SlackSocketLive.layer,
      botToken.toLayer,
      appToken.toLayer,
      default
    )

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[Has[Slack] with Has[AppToken] with Has[AccessToken] with Has[SlackSocket] with Has[
    BasicConfig
  ] with Console, SlackError, Unit] =
    // Test that we can receive messages
    SlackSocket().collectM {
      case Left(_: Hello)                                                  =>
        putStrLn("Said Hello").orDie
      case Right(Event(_, _, _, UserTyping(channel, user), _, _, _, _, _)) =>
        putStrLn(s"User $user is typing in $channel").orDie
      case _                                                               => ZIO.unit
    }.runDrain.toManaged_

  val testApi: ZIO[Has[Slack] with Has[AccessToken] with Has[BasicConfig], SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- Slack.postChatMessage(config.channel, text = Some("Hello Slack client!")).map(_.toEither).absolve
      _      <- Slack.addReactionToMessage("+1", config.channel, resp.ts)
    } yield resp.ts

}
