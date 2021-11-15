package basic

import com.github.dapperware.slack.api.web
import com.github.dapperware.slack.SlackClient
import com.github.dapperware.slack.realtime.models.{ SendMessage, UserTyping }
import com.github.dapperware.slack.realtime.{ SlackRealtimeClient, SlackRealtimeEnv }
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import common.{ accessToken, default, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.stream.ZStream
import zio.{ App, ExitCode, Has, Layer, ZIO, ZManaged }
import com.github.dapperware.slack.AccessToken

object BasicApp extends App {

  val accessTokenAndBasic: Layer[Throwable, Has[AccessToken] with Has[BasicConfig]] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, Has[SlackClient] with Has[SlackRealtimeClient]] =
    AsyncHttpClientZioBackend.layer() >>> (SlackClient.live ++ SlackRealtimeClient.live)

  val layers: Layer[Throwable, SlackEnv with SlackRealtimeEnv with Has[BasicConfig]] =
    slackClients ++ accessTokenAndBasic

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[SlackRealtimeEnv with Has[BasicConfig] with Console, SlackError, Unit] =
    for {
      config   <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      receiver <- SlackRealtimeClient.connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
      _        <- receiver.collectM {
                    case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel")
                    case _                         => ZIO.unit
                  }.runDrain.toManaged_
    } yield ()

  val testApi: ZIO[SlackEnv with Has[BasicConfig], SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- web.postChatMessage(config.channel, text = "Hello Slack client!")
      _      <- web.addReactionToMessage("+1", config.channel, resp)
    } yield resp

}
