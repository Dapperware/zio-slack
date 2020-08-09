package search

import common.{ accessToken, default }
import slack.api.search.searchMessages
import slack.client.SlackClient
import slack.realtime.SlackRealtimeClient
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.stream.{ ZSink, ZStream }
import zio.{ App, ExitCode, ZIO }

object SearchApp extends App {
  val layers = AsyncHttpClientZioBackend.layer() >>>
    (SlackClient.live ++ SlackRealtimeClient.live ++ (default >+> accessToken.live))

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    ZStream
      .repeatEffect(interactionLoop)
      .run(ZSink.foreach(response => putStrLn(response.spaces2)))
      .provideCustomLayer(layers)
      .exitCode

  val interactionLoop = for {
    input  <- getStrLn
    result <- searchMessages(input, count = Some(10))
  } yield result
}
