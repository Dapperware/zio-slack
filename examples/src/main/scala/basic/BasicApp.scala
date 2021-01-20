package basic

import com.dapperware.slack.api.web
import com.dapperware.slack.client.SlackClient
import com.dapperware.slack.realtime.models.{SendMessage, UserTyping}
import com.dapperware.slack.realtime.{SlackRealtimeClient, SlackRealtimeEnv}
import com.dapperware.slack.{SlackEnv, SlackError, realtime}
import common.{Basic, BasicConfig, accessToken, default}
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.config._
import zio.console.{Console, putStrLn}
import zio.stream.ZStream
import zio.{App, ExitCode, Layer, ZIO, ZManaged}

object BasicApp extends App {
  val layers: Layer[Throwable, SlackEnv with SlackRealtimeEnv with Basic] =
    AsyncHttpClientZioBackend.layer() >>>
      (SlackClient.live ++ SlackRealtimeClient.live ++ (default >+> accessToken.live))

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[SlackRealtimeEnv with ZConfig[BasicConfig] with Console, SlackError, Unit] =
    for {
      config <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      receiver <- realtime.connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
      _ <- receiver.collectM {
            case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel")
            case _                         => ZIO.unit
          }.runDrain.toManaged_
    } yield ()

  val testApi: ZIO[SlackEnv with ZConfig[BasicConfig], SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- web.postChatMessage(config.channel, text = "Hello Slack client!")
      _      <- web.addReactionToMessage("+1", config.channel, resp)
    } yield resp

}
