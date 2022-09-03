package joke

import com.github.dapperware.slack.models.Channel
import com.github.dapperware.slack.{ AccessToken, Slack, SlackError }
import common.botToken
import io.circe
import io.circe.{ DecodingFailure, Json }
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client3.circe.asJson
import sttp.client3.{ basicRequest, Request, ResponseException, SttpBackend, UriContext }
import zio._
import zio.stream.ZStream

/**
 * Every 3 hours, randomly pick a channel that the bot is part of and send a chuck norris joke to it.
 */
object JokeApp extends ZIOAppDefault {

  val getJokeReq: Request[Either[ResponseException[String, circe.Error], Either[DecodingFailure, String]], Any] =
    basicRequest
      .get(uri"https://api.chucknorris.io/jokes/random")
      .response(asJson[Json])
      .mapResponseRight(_.hcursor.downField("value").as[String])

  val getJoke = ZIO
    .serviceWithZIO[SttpBackend[Task, Any]](_.send(getJokeReq))
    .map(_.body)
    .absolve
    .absolve
    .mapError(SlackError.fromThrowable)

  // Shuffle the conversations that we are a part of
  val shuffledConversations: ZIO[Slack with AccessToken, SlackError, List[Channel]] =
    ZStream
      .paginateZIO(Option.empty[String]) { cursor =>
        for {
          convos <- Slack.listConversations(cursor = cursor).map(_.toEither).absolve
        } yield (
          Chunk.fromIterable(convos.channels).filter(_.is_member.contains(true)),
          convos.response_metadata.next_cursor.filter(_.nonEmpty).map(Some(_))
        )
      }
      .flattenChunks
      .runCollect
      .flatMap(c => Random.shuffle(c.toList))

  val result: ZIO[Slack with AccessToken with SttpBackend[Task, Any], SlackError, Unit] =
    for {
      shuffled <- shuffledConversations
      _        <- ZIO.foreachDiscard(shuffled) { channel =>
                    (for {
                      joke <- getJoke
                      _    <- Slack.postChatMessage(channel.id, text = Some(joke)).map(_.toEither).absolve
                    } yield ()) *> ZIO.sleep(3.hours)
                  }
    } yield ()

  override val run =
    result
      .provide(
        common.default,
        botToken,
        Slack.http,
        AsyncHttpClientZioBackend.layer()
      )
}
