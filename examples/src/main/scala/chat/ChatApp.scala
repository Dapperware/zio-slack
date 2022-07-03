package chat

import com.github.dapperware.slack.models.{ Message, SendMessage }
import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.{ AccessToken, HttpSlack, Slack, SlackSocket, SlackSocketLive }
import common.{ botToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console._
import zio.stream.ZStream
import zio.magic._

/**
 * A simple interactive application to show how to use slack zio for much profit
 */
object ChatApp extends App {
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

  val layers: Layer[Throwable, Has[Slack] with Has[SlackSocket] with Has[BasicConfig] with Has[AccessToken]] =
    ZLayer.fromMagic[Has[Slack] with Has[SlackSocket] with Has[BasicConfig] with Has[AccessToken]](
      AsyncHttpClientZioBackend.layer(),
      Slack.http,
      SlackSocketLive.layer,
      default,
      botToken.toLayer
    )

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val messageSender = ZStream
      .fromEffect(for {
        input   <- getStrLn
        channel <- ZIO.service[BasicConfig].map(_.channel)
        message  = SendMessage(channel, input)
      } yield message)
      .forever
      .toQueueUnbounded

    val chatStack = for {
      outgoing <- messageSender
    } yield SlackSocket.connect(ZStream.fromQueue(outgoing).forever.flattenTake)

    chatStack.use { receiver =>
      for {
        socketFiber <- receiver.collectM { case Message(_, channel, user, text, _, _) =>
                         val references = ZIO.foreach(findReferences(text)) { ref =>
                           Slack.getUserInfo(ref).map(_.toEither.map(ref -> _.user.name)).absolve
                         }
                         (Slack.getConversationInfo(channel).map(_.toEither).absolve <&>
                           Slack.getUserInfo(user).map(_.toEither).absolve <&>
                           references).flatMap { case ((c, u), r) =>
                           putStrLn(s"${c.name}: ${u.user.name} -> ${replaceReferences(text, r)}")
                         }
                       }.runDrain.fork
        _           <- putStrLn("Ready for input!")
        _           <- socketFiber.join
      } yield ()
    }.provideCustomLayer(layers).exitCode
  }
}
