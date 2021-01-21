package search

import com.dapperware.slack.api.web.searchMessages
import com.dapperware.slack.client.SlackClient
import com.dapperware.slack.realtime.SlackRealtimeClient
import common.{accessToken, default}
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.stream.{ZSink, ZStream}
import zio.{App, ExitCode, ZIO}

object SearchApp extends App {
  val layers = AsyncHttpClientZioBackend.layer() >>>
    (SlackClient.live ++ SlackRealtimeClient.live ++ (default >+> accessToken.toLayer))

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
