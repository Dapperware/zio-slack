package search

import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.{ AccessToken, HttpSlack, Slack }
import common.{ accessToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.stream.{ ZSink, ZStream }
import zio.{ App, ExitCode, Has, Layer, ZIO }

object SearchApp extends App {

  val accessTokenAndBasic: Layer[Throwable, Has[AccessToken] with Has[BasicConfig]] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, Has[Slack] with Has[SlackRealtimeClient]] =
    AsyncHttpClientZioBackend.layer() >>> (HttpSlack.layer ++ SlackRealtimeClient.live)

  val layers = slackClients ++ accessTokenAndBasic

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    ZStream
      .repeatEffect(interactionLoop)
      .run(ZSink.foreach(response => putStrLn(response.spaces2)))
      .provideCustomLayer(layers)
      .exitCode

  val interactionLoop = for {
    input  <- getStrLn.orDie
    result <- Slack.searchMessages(input, count = Some(10)).map(_.toEither).absolve
  } yield result
}
