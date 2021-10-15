package search

import com.github.dapperware.slack.api.web.searchMessages
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.realtime.SlackRealtimeClient
import common.{ accessToken, default, Basic }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.stream.{ ZSink, ZStream }
import zio.{ App, ExitCode, ZIO, Layer, Has}
import com.github.dapperware.slack.Token

object SearchApp extends App {

  val accessTokenAndBasic: Layer[Throwable, Has[Token] with Basic] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, Has[SlackClient.Service] with Has[SlackRealtimeClient.Service]] = AsyncHttpClientZioBackend.layer() >>> (SlackClient.live ++ SlackRealtimeClient.live)

  val layers = slackClients ++ accessTokenAndBasic

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
