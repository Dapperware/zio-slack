package joke

import com.github.dapperware.slack
import com.github.dapperware.slack.api.web.{ listConversations, postChatMessage }
import com.github.dapperware.slack.models.Channel
import com.github.dapperware.slack.{ AccessToken, SlackClient, SlackEnv }
import common.{ accessToken, BasicConfig }
import io.circe
import io.circe.{ DecodingFailure, Json }
import sttp.client3.asynchttpclient.zio.{ send, AsyncHttpClientZioBackend, SttpClient }
import sttp.client3.circe.asJson
import sttp.client3.{ basicRequest, Request, ResponseException, UriContext }
import zio._
import zio.clock.Clock
import zio.duration._
import zio.random.Random
import zio.stream.ZStream

/**
 * Every 3 hours, randomly pick a channel that the bot is part of and send a chuck norris joke to it.
 */
object JokeApp extends App {

  val getJoke: Request[Either[ResponseException[String, circe.Error], Either[DecodingFailure, String]], Any] =
    basicRequest
      .get(uri"https://api.chucknorris.io/jokes/random")
      .response(asJson[Json])
      .mapResponseRight(_.hcursor.downField("value").as[String])

  // Shuffle the conversations that we are a part of
  val shuffledConversations: ZIO[Random with slack.SlackEnv, Throwable, List[Channel]] =
    ZStream
      .paginateM(Option.empty[String]) { cursor =>
        for {
          convos <- listConversations(cursor)
        } yield (
          Chunk.fromIterable(convos.items).filter(_.is_member.contains(true)),
          convos.response_metadata.flatMap(_.next_cursor).filter(_.nonEmpty).map(Some(_))
        )
      }
      .flattenChunks
      .runCollect
      .flatMap(c => random.shuffle(c.toList))

  val accessTokenLayer: Layer[Throwable, Has[AccessToken] with Has[BasicConfig]] =
    (common.default >+> accessToken.toLayer)

  val layers: ZLayer[Any, Throwable, SttpClient with Has[SlackClient] with Has[AccessToken] with Has[BasicConfig]] =
    (AsyncHttpClientZioBackend.layer() >+> (SlackClient.live ++ accessTokenLayer))

  val result: ZIO[SttpClient with SlackEnv with Random with Clock with Has[BasicConfig], Throwable, Unit] =
    for {
      shuffled <- shuffledConversations
      _        <- ZIO.foreach_(shuffled) { channel =>
                    (for {
                      resp <- send(getJoke)
                      body <- IO.fromEither(resp.body)
                      joke <- IO.fromEither(body)
                      _    <- postChatMessage(channel.id, joke)
                    } yield ()) *> ZIO.sleep(3.hours)
                  }
    } yield ()

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    result
      .provideCustomLayer(layers)
      .exitCode
}
