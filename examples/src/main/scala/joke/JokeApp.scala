package joke

import com.github.dapperware.slack
import com.github.dapperware.slack.api.web.{ listConversations, postChatMessage }
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.models.Channel
import com.github.dapperware.slack.withAccessTokenM
import common.{ accessToken, Basic }
import io.circe
import io.circe.{ DecodingFailure, Json }
import sttp.client._
import sttp.client.asynchttpclient.zio.{ AsyncHttpClientZioBackend, SttpClient }
import sttp.client.circe._
import zio._
import zio.clock.Clock
import zio.duration._
import zio.random.Random
import zio.stream.ZStream

/**
 * Every 3 hours, randomly pick a channel that the bot is part of and send a chuck norris joke to it.
 */
object JokeApp extends App {

  val getJoke: Request[Either[ResponseError[circe.Error], Either[DecodingFailure, String]], Nothing] = basicRequest
    .get(uri"https://api.chucknorris.io/jokes/random")
    .response(asJson[Json])
    .mapResponseRight(_.hcursor.downField("value").as[String])

  // Shuffle the conversations that we are a part of
  val shuffledConversations: ZIO[Random with slack.SlackEnv, Throwable, List[Channel]] =
    ZStream
      .paginateM(Option.empty[String]) { cursor =>
        for {
          convos <- listConversations(cursor)
        } yield
          (Chunk.fromIterable(convos.items).filter(_.is_member.contains(true)),
           convos.response_metadata.flatMap(_.next_cursor).filter(_.nonEmpty).map(Some(_)))
      }
      .flattenChunks
      .runCollect
      .flatMap(c => random.shuffle(c.toList))

  val layers: ZLayer[Any, Throwable, SttpClient with SlackClient with Basic] =
    AsyncHttpClientZioBackend.layer() >+> SlackClient.live ++ common.default

  val result: ZIO[SttpClient with SlackClient with Random with Clock with Basic, Throwable, Unit] =
    withAccessTokenM(accessToken)(for {
      shuffled <- shuffledConversations
      _ <- ZIO.foreach(shuffled) { channel =>
            (for {
              resp <- SttpClient.send(getJoke)
              body <- IO.fromEither(resp.body)
              joke <- IO.fromEither(body)
              _    <- postChatMessage(channel.id, joke)
            } yield ()) *> ZIO.sleep(3.hours)
          }
    } yield ())

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    result
      .provideCustomLayer(layers)
      .exitCode
}
