package joke

import basic.BasicConfig
import io.circe
import io.circe.{ DecodingFailure, Json }
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.AccessToken
import slack.api.chats._
import slack.api.conversations._
import slack.client.SlackClient
import slack.models.Channel
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
          (Chunk.fromIterable(convos.channels).filter(_.is_member.contains(true)),
           convos.response_metadata.flatMap(_.next_cursor).filter(_.nonEmpty).map(Some(_)))
      }
      .flattenChunks
      .runCollect
      .flatMap(random.shuffle(_))

  val configLayer = ZLayer.fromEffect {
    ZIO
      .fromEither(ConfigSource.defaultApplication.at("basic").load[BasicConfig])
      .bimap(ConfigReaderException(_), _.token)
      .flatMap(AccessToken.make)
  }

  val layers =
    AsyncHttpClientZioBackend.layer() >+> (SlackClient.live ++ configLayer)

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
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
      .provideSomeLayer[Clock with Random](layers)
      .exitCode
}
