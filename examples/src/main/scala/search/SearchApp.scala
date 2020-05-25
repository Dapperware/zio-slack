package search

import basic.BasicConfig
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import zio.{ App, ZIO, ZLayer }
import zio.console._
import slack.api.search.searchMessages
import slack.core.AccessToken
import slack.core.client.SlackClient
import slack.realtime.SlackRealtimeClient
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.stream.{ ZSink, ZStream }

object SearchApp extends App {
  val layers = AsyncHttpClientZioBackend.layer() >>>
    (SlackClient.live ++ SlackRealtimeClient.live)

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    ZStream
      .repeatEffect(interactionLoop)
      .run(ZSink.foreach(response => putStrLn(response.spaces2)))
      .provideCustomLayer(
        ZLayer.fromEffect(
          ZIO
            .fromEither(ConfigSource.defaultApplication.at("basic").load[BasicConfig])
            .bimap(ConfigReaderException(_), _.token)
            .flatMap(AccessToken.make)
        ) ++ layers
      )
      .fold(_ => 1, _ => 0)

  val interactionLoop = for {
    input  <- getStrLn
    result <- searchMessages(input, count = Some(10))
  } yield result
}
