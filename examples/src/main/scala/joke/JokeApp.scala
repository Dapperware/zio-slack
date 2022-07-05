package joke

import com.github.dapperware.slack.models.Channel
import com.github.dapperware.slack.{ AccessToken, Slack, SlackError }
import common.{ botToken, BasicConfig }
import io.circe
import io.circe.{ DecodingFailure, Json }
import sttp.client3.asynchttpclient.zio.{ send, AsyncHttpClientZioBackend, SttpClient }
import sttp.client3.circe.asJson
import sttp.client3.{ basicRequest, Request, ResponseException, UriContext }
import zio._
import zio.clock.Clock
import zio.duration._
import zio.magic._
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
  val shuffledConversations: ZIO[Random with Has[Slack] with Has[AccessToken], SlackError, List[Channel]] =
    ZStream
      .paginateM(Option.empty[String]) { cursor =>
        for {
          convos <- Slack.listConversations(cursor = cursor).map(_.toEither).absolve
        } yield (
          Chunk.fromIterable(convos.channels).filter(_.is_member.contains(true)),
          convos.response_metadata.next_cursor.filter(_.nonEmpty).map(Some(_))
        )
      }
      .flattenChunks
      .runCollect
      .flatMap(c => random.shuffle(c.toList))

  val layers: ZLayer[Any, Throwable, SttpClient with Has[Slack] with Has[AccessToken] with Has[BasicConfig]] =
    ZLayer.fromMagic[SttpClient with Has[Slack] with Has[AccessToken] with Has[BasicConfig]](
      common.default,
      botToken.toLayer,
      Slack.http,
      AsyncHttpClientZioBackend.layer()
    )

  val result: ZIO[Clock with Has[Slack] with Has[AccessToken] with SttpClient with Random, SlackError, Unit] =
    for {
      shuffled <- shuffledConversations
      _        <- ZIO.foreach_(shuffled) { channel =>
                    (for {
                      resp <- send(getJoke).mapError(SlackError.fromThrowable)
                      body <- IO.fromEither(resp.body).mapError(SlackError.fromThrowable)
                      joke <- IO.fromEither(body).mapError(SlackError.fromThrowable)
                      _    <- Slack.postChatMessage(channel.id, text = Some(joke)).map(_.toEither).absolve
                    } yield ()) *> ZIO.sleep(3.hours)
                  }
    } yield ()

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    result
      .provideCustomLayer(layers)
      .exitCode
}
