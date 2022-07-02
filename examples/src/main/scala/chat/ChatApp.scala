package chat

import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.realtime.models.{ Message, SendMessage }
import com.github.dapperware.slack.{ AccessToken, HttpSlack, Slack }
import common.{ accessToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console._
import zio.stream.ZStream

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

  val accessTokenAndBasic: Layer[Throwable, Has[AccessToken] with Has[BasicConfig]] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, Has[Slack] with Has[SlackRealtimeClient]] =
    AsyncHttpClientZioBackend.layer() >>> (HttpSlack.layer ++ SlackRealtimeClient.live)

  private val layers: ZLayer[Any, Throwable, Has[Slack] with Has[SlackRealtimeClient] with Has[BasicConfig]] =
    slackClients ++ accessTokenAndBasic

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
      receiver <- SlackRealtimeClient.connect(ZStream.fromQueue(outgoing).forever.flattenTake)
    } yield receiver

    chatStack.use { receiver =>
      for {
        socketFiber <- receiver.collectM { case Message(_, channel, user, text, _, _) =>
                         val references = ZIO.foreach(findReferences(text)) { ref =>
                           Slack.getUserInfo(ref).map(_.toEither.map(ref -> _.user.name)).absolve
                         }
                         (Slack.getConversationInfo(channel) <&> (Slack.getUserInfo(user) <&> references)).flatMap {
                           case (c, (u, r)) =>
                             putStrLn(s"${c.name}: ${u.name} -> ${replaceReferences(text, r)}")
                         }
                       }.runDrain.fork
        _           <- putStrLn("Ready for input!")
        _           <- socketFiber.join
      } yield ()
    }.provideCustomLayer(layers).exitCode
  }
}
