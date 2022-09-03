package basic

import com.github.dapperware.slack.models.events.SocketEventPayload.Event
import com.github.dapperware.slack.models.events.{ Hello, UserTyping }
import com.github.dapperware.slack.{ AccessToken, AppToken, Slack, SlackError, SlackSocket, SlackSocketLive }
import common.{ appToken, botToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.Console.printLine
import zio.{ ExitCode, ZIO, ZIOAppDefault }

object BasicApp extends ZIOAppDefault {

  override def run =
    (for {
      resp <- (testApi <&> testRealtime)
    } yield resp).provide(
      AsyncHttpClientZioBackend.layer().orDie,
      Slack.http,
      SlackSocketLive.layer,
      botToken,
      appToken,
      default
    )

  val testRealtime: ZIO[Slack with AppToken with AccessToken with SlackSocket with BasicConfig, SlackError, Unit] =
    // Test that we can receive messages
    SlackSocket().collectZIO {
      case Left(_: Hello)                                                  =>
        printLine("Said Hello").orDie
      case Right(Event(_, _, _, UserTyping(channel, user), _, _, _, _, _)) =>
        printLine(s"User $user is typing in $channel").orDie
      case _                                                               => ZIO.unit
    }.runDrain

  val testApi: ZIO[Slack with AccessToken with BasicConfig, SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- Slack.postChatMessage(config.channel, text = Some("Hello Slack client!")).map(_.toEither).absolve
      _      <- Slack.addReactionToMessage("+1", config.channel, resp.ts)
    } yield resp.ts

}
