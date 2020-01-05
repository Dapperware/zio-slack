package chat

import basic.BasicConfig
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.{ AccessToken, SlackClient }
import slack.realtime.SlackRealtimeClient
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.{ ManagedApp, NeedsEnv, Queue, ZIO, ZManaged }
import zio.console._
import slack.realtime._
import slack.realtime.models.{ Message, OutboundMessage, SendMessage }
import zio.stream.ZStream
import zio.macros.delegate.syntax._
import zio.macros.delegate._
import slack.users._
import slack.channels._

/**
 * Pretend we have the next version of ZIO
 */
trait EnrichedManaged {

  implicit def managedOps[R, E, A](managed: ZManaged[R, E, A]): EnrichedManaged.Ops[R, E, A] =
    new EnrichedManaged.Ops[R, E, A](managed)

}

object EnrichedManaged {
  class Ops[-R, +E, +A](managed: ZManaged[R, E, A]) {
    def provideSomeM[R0, E1 >: E](r0: ZIO[R0, E1, R])(implicit ev: NeedsEnv[R]): ZManaged[R0, E1, A] =
      r0.toManaged_.flatMap(managed.provide)

    def provideSomeManaged[R0, E1 >: E](r0: ZManaged[R0, E1, R])(implicit ev: NeedsEnv[R]): ZManaged[R0, E1, A] =
      r0.flatMap(managed.provide)
  }
}

/**
 * A simple interactive application to show how to use slack zio for much profit
 */
object ChatApp extends ManagedApp with EnrichedManaged {
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

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      backend <- AsyncHttpClientZioBackend().toManaged(_.close().orDie)
      source  = ConfigSource.defaultApplication
      config <- ZManaged
                 .fromEither(source.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      env = ZIO.environment[zio.ZEnv] @@
        enrichWithM(SlackRealtimeClient.make(backend)) @@
        enrichWithM(SlackClient.make(backend)) @@
        enrichWithM(AccessToken.make(config.token))
      _ <- (for {
            outgoing <- Queue.unbounded[OutboundMessage].toManaged_
            senderFiber <- (for {
                            input   <- getStrLn
                            message = SendMessage(config.channel, input)
                            _       <- outgoing.offer(message)
                          } yield ()).forever.toManaged_.fork
            socketFiber <- (for {
                            receiver <- realtime.connect(ZStream.fromQueue(outgoing).forever)
                            _ <- receiver.collectM {
                                  case Message(ts, channel, user, text, is_starred, thread_ts) =>
                                    val references = ZIO.traverse(findReferences(text)) { ref =>
                                      getUserInfo(ref).map { ref -> _.name }
                                    }
                                    (getChannelInfo(channel) <&> (getUserInfo(user) <&> references)).flatMap {
                                      case (c, (u, r)) =>
                                        putStrLn(s"${c.name}: ${u.name} -> ${replaceReferences(text, r)}")
                                    }
                                }.runDrain
                          } yield ()).toManaged_.fork
            _ <- putStrLn("Ready for input!").toManaged_
            _ <- (senderFiber <*> socketFiber).join.toManaged_
          } yield ()).provideSomeM(env)
    } yield ()).fold(_ => 1, _ => 0)
}
