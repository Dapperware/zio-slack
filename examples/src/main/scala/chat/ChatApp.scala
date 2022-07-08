package chat

import com.github.dapperware.slack.models.events.Message
import com.github.dapperware.slack.models.events.SocketEventPayload.Event
import com.github.dapperware.slack.{ Slack, SlackSocket, SlackSocketLive }
import common.{ appToken, botToken, default }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.Console.printLine
import zio._

/**
 * A simple interactive application to show how to use slack zio for much profit
 */
object ChatApp extends ZIOAppDefault {
  private val referenceRegex = "<@(\\w*)>".r

  private def findReferences(message: String): List[String] = {
    val results = referenceRegex.findAllIn(message)
    val builder = List.newBuilder[String]
    while (results.hasNext) {
      results.next
      builder += results.group(1)
    }

    builder.result()
  }

  private def replaceReferences(text: String, refMap: List[(String, String)]): String =
    refMap.foldLeft(text) { case (t, (ref, replace)) =>
      t.replaceAllLiterally(s"<@$ref>", s"@$replace")
    }

  override def run: ZIO[Any, Nothing, ExitCode] =
    (for {
      fib <-
        SlackSocket().collectZIO { case Right(Event(_, _, _, Message(_, channel, user, text, _, _), _, _, _, _, _)) =>
          val references = ZIO.foreach(findReferences(text)) { ref =>
            Slack.getUserInfo(ref).map(_.toEither.map(ref -> _.user.name)).absolve
          }
          (Slack.getConversationInfo(channel).map(_.toEither).absolve <&>
            Slack.getUserInfo(user).map(_.toEither).absolve <&>
            references).flatMap { case (c, u, r) =>
            printLine(s"${c.name}: ${u.user.name} -> ${replaceReferences(text, r)}").orDie
          }
        }.runDrain
          .tapError(e => printLine(e.toString).orDie)
          .fork
      _   <- Console.readLine *> fib.interrupt
    } yield ())
      .provide(AsyncHttpClientZioBackend.layer(), Slack.http, SlackSocketLive.layer, default, botToken, appToken)
      .exitCode
}
