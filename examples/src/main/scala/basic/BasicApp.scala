package basic

import com.github.dapperware.slack.api.web
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.realtime.models.{ SendMessage, UserTyping }
import com.github.dapperware.slack.realtime.{ SlackRealtimeClient, SlackRealtimeEnv }
import com.github.dapperware.slack.{ realtime, SlackEnv, SlackError }
import common.{ accessToken, default, Basic, BasicConfig }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.console.{ putStrLn, Console }
import zio.stream.ZStream
import zio.{ App, ExitCode, Layer, ZIO, ZManaged }
import com.github.dapperware.slack.AccessToken

object BasicApp extends App {

  val accessTokenAndBasic: Layer[Throwable, AccessToken with Basic] = default >+> accessToken.toLayer

  val slackClients: Layer[Throwable, SlackClient with SlackRealtimeClient] = 
    AsyncHttpClientZioBackend.layer() >>> (SlackClient.live ++ SlackRealtimeClient.live)

  val layers: Layer[Throwable, SlackEnv with SlackRealtimeEnv with Basic] =
    slackClients +!+ accessTokenAndBasic

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layers)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[SlackRealtimeEnv with Basic with Console, SlackError, Unit] =
    for {
      config   <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      receiver <- realtime.connect(ZStream(SendMessage(config.channel, "Hi realtime!")))
      _        <- receiver.collectM {
                    case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel")
                    case _                         => ZIO.unit
                  }.runDrain.toManaged_
    } yield ()

  val testApi: ZIO[SlackEnv with Basic, SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- web.postChatMessage(config.channel, text = "Hello Slack client!")
      _      <- web.addReactionToMessage("+1", config.channel, resp)
    } yield resp

}
