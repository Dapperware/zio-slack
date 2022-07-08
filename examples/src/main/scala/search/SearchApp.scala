package search

import com.github.dapperware.slack.Slack
import common.userToken
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.Console.{ printLine, readLine }
import zio.stream.{ ZSink, ZStream }
import zio.{ ExitCode, ZIO, ZIOAppArgs, ZIOAppDefault }

object SearchApp extends ZIOAppDefault {

  val run: ZIO[ZIOAppArgs, Nothing, ExitCode] =
    ZStream
      .repeatZIO(interactionLoop)
      .run(ZSink.foreach(response => printLine(response.spaces2)))
      .provide(AsyncHttpClientZioBackend.layer(), Slack.http, common.default, userToken)
      .exitCode

  val interactionLoop = for {
    input  <- readLine.orDie
    result <- Slack.searchMessages(input, count = Some(10)).map(_.toEither).absolve
  } yield result
}
