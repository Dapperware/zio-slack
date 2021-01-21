package chat

import com.github.dapperware.slack.api.web.{ getConversationInfo, getUserInfo }
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.{ realtime, AccessToken }
import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.realtime.models.{ Message, SendMessage }
import common.{ accessToken, default, Basic, BasicConfig }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
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
    refMap.foldLeft(text) {
      case (t, (ref, replace)) => t.replaceAllLiterally(s"<@$ref>", s"@$replace")
    }

  private val layers: ZLayer[Any, Throwable, SlackRealtimeClient with SlackClient with AccessToken with Basic] =
    AsyncHttpClientZioBackend
      .layer() >>> (SlackRealtimeClient.live ++ SlackClient.live ++ (default >+> accessToken.toLayer))

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val messageSender = ZStream
      .fromEffect(for {
        input   <- getStrLn
        channel <- ZIO.service[BasicConfig].map(_.channel)
        message = SendMessage(channel, input)
      } yield message)
      .forever
      .toQueueUnbounded

    val chatStack = for {
      outgoing <- messageSender
      receiver <- realtime.connect(ZStream.fromQueue(outgoing).forever.flattenTake)
    } yield receiver

    chatStack.use { receiver =>
      for {
        socketFiber <- receiver.collectM {
                        case Message(_, channel, user, text, _, _) =>
                          val references = ZIO.foreach(findReferences(text)) { ref =>
                            getUserInfo(ref).map { ref -> _.name }
                          }
                          (getConversationInfo(channel) <&> (getUserInfo(user) <&> references)).flatMap {
                            case (c, (u, r)) =>
                              putStrLn(s"${c.name}: ${u.name} -> ${replaceReferences(text, r)}")
                          }
                      }.runDrain.fork
        _ <- putStrLn("Ready for input!")
        _ <- socketFiber.join
      } yield ()
    }.provideCustomLayer(layers).exitCode
  }
}
