package search

import com.github.dapperware.slack.realtime.SlackRealtimeClient
import com.github.dapperware.slack.{ AccessToken, HttpSlack, Slack }
import common.{ botToken, userToken, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console._
import zio.magic._
import zio.stream.{ ZSink, ZStream }
import zio.{ App, ExitCode, Has, Layer, ZIO, ZLayer }

object SearchApp extends App {

  val layers: Layer[Throwable, Has[Slack] with Has[SlackRealtimeClient] with Has[AccessToken] with Has[BasicConfig]] =
    ZLayer.fromMagic[Has[Slack] with Has[SlackRealtimeClient] with Has[AccessToken] with Has[BasicConfig]](
      AsyncHttpClientZioBackend.layer(),
      Slack.http,
      SlackRealtimeClient.live,
      common.default,
      userToken.toLayer
    )

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
