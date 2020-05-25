package joke

import basic.BasicConfig
import io.circe
import io.circe.{ DecodingFailure, Json }
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.api.chats._
import slack.api.conversations._
import slack.core.AccessToken
import slack.core.client.SlackClient
import slack.models.Channel
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio._
import zio.clock.Clock
import zio.duration._
import zio.random.Random
import zio.stream.ZStream

/**
 * Every 3 hours, randomly pick a channel that the bot is part of and send a chuck norris joke to it.
 */
object JokeApp extends ManagedApp {

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
          (Chunk.fromIterable(convos.channels).filter(_.is_member.contains(true)),
           convos.response_metadata.map(_.next_cursor))
      }
      .flattenChunks
      .runCollect
      .flatMap(random.shuffle(_))

  val layers = AsyncHttpClientZioBackend.layer() >>> SlackClient.live

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      backend <- AsyncHttpClientZioBackend().toManaged(_.close.orDie)
      config <- ZManaged
                 .fromEither(ConfigSource.defaultApplication.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      environment = AccessToken.make(config.token).toLayer ++ layers
      _ <- (for {
            shuffled <- shuffledConversations
            _ <- ZIO.foreach_(shuffled) { channel =>
                  (for {
                    resp <- backend.send(getJoke)
                    body <- IO.fromEither(resp.body)
                    joke <- IO.fromEither(body)
                    _    <- postChatMessage(channel.id, joke)
                  } yield ()) *> ZIO.sleep(3.hours)
                }
          } yield ()).provideSomeLayer[Clock with Random](environment).toManaged_
    } yield ()).fold(_ => 1, _ => 0)
}
