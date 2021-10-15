package chat

import com.github.dapperware.slack.api.web.{ getConversationInfo, getUserInfo }
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.realtime.models.{ Message, SendMessage }
import com.github.dapperware.slack.realtime
import common.{ accessToken, default, Basic, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console._
import zio.stream.ZStream
import com.github.dapperware.slack.Token

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

  val accessTokenAndBasic: Layer[Throwable, Has[Token] with Basic] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, Has[SlackClient.Service] with Has[SlackRealtimeClient.Service]] = 
    AsyncHttpClientZioBackend.layer() >>> (SlackClient.live ++ SlackRealtimeClient.live)

  private val layers: ZLayer[Any, Throwable, Has[SlackClient.Service] with Has[SlackRealtimeClient.Service] with Has[Token] with Basic] =
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
      receiver <- realtime.connect(ZStream.fromQueue(outgoing).forever.flattenTake)
    } yield receiver

    chatStack.use { receiver =>
      for {
        socketFiber <- receiver.collectM { case Message(_, channel, user, text, _, _) =>
                         val references = ZIO.foreach(findReferences(text)) { ref =>
                           getUserInfo(ref).map(ref -> _.name)
                         }
                         (getConversationInfo(channel) <&> (getUserInfo(user) <&> references)).flatMap {
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
