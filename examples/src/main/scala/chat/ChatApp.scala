package chat

import basic.BasicConfig
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.api.conversations._
import slack.api.realtime
import slack.api.users._
import slack.realtime.SlackRealtimeClient
import slack.realtime.models.{ Message, OutboundMessage, SendMessage }
import slack.SlackError
import slack.core.AccessToken
import slack.core.client.SlackClient
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.stream.ZStream
import zio.{ ManagedApp, ZIO, ZManaged }

/**
 * A simple interactive application to show how to use slack zio for much profit
 */
object ChatApp extends ManagedApp {
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

  private val layers = AsyncHttpClientZioBackend.layer() >>> (SlackRealtimeClient.live ++ SlackClient.live)

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      config <- ZManaged
                 .fromEither(ConfigSource.defaultApplication.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      env = layers ++ AccessToken.make(config.token).toLayer
      _ <- (for {
            outgoing <- ZStream
                         .fromEffect(for {
                           input   <- getStrLn
                           message = SendMessage(config.channel, input)
                         } yield message)
                         .forever
                         .toQueueUnbounded[SlackError, OutboundMessage]
            socketFiber <- (for {
                            receiver <- realtime.connect(ZStream.fromQueue(outgoing).forever.unTake)
                            _ <- receiver.collectM {
                                  case Message(_, channel, user, text, _, _) =>
                                    val references = ZIO.foreach(findReferences(text)) { ref =>
                                      getUserInfo(ref).map { ref -> _.name }
                                    }
                                    (getConversationInfo(channel) <&> (getUserInfo(user) <&> references)).flatMap {
                                      case (c, (u, r)) =>
                                        putStrLn(s"${c.name}: ${u.name} -> ${replaceReferences(text, r)}")
                                    }
                                }.runDrain.toManaged_
                          } yield ()).fork
            _ <- putStrLn("Ready for input!").toManaged_
            _ <- socketFiber.join.toManaged_
          } yield ()).provideSomeLayer[Console](env)
    } yield ()).fold(_ => 1, _ => 0)
}
