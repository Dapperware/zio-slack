package joke

import basic.BasicConfig
import io.circe
import io.circe.{ DecodingFailure, Json }
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import slack.api.chats._
import slack.api.conversations._
import slack.core.access.AccessToken
import slack.core.client.SlackClient
import slack.models.Channel
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio.clock.Clock
import zio.duration._
import zio.random.Random
import zio.stream.{ ZStream, ZStreamChunk }
import zio.{ Chunk, IO, ManagedApp, Schedule, ZManaged }

object JokeApp extends ManagedApp {

  val getJoke: Request[Either[ResponseError[circe.Error], Either[DecodingFailure, String]], Nothing] = basicRequest
    .get(uri"https://api.chucknorris.io/jokes/random")
    .response(asJson[Json])
    .mapResponseRight(_.hcursor.downField("value").as[String])

  def boundedRandom(max: Int) =
    zio.random.nextInt.map(_.abs % max)

  // Randomly pick a conversation we are a member of
  val randomConversation =
    ZStreamChunk(ZStream.paginateM(Option.empty[String]) { cursor =>
      for {
        convos <- listConversations(cursor)
      } yield
        (Chunk.fromIterable(convos.channels).filter(_.is_member.contains(true)),
         convos.response_metadata.map(_.next_cursor))
    }).runCollect.flatMap { convos =>
      boundedRandom(convos.length).map(convos(_))
    }

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] =
    (for {
      backend <- AsyncHttpClientZioBackend().toManaged(_.close.orDie)
      source  = ConfigSource.defaultApplication
      config <- ZManaged
                 .fromEither(source.at("basic").load[BasicConfig])
                 .mapError(ConfigReaderException(_))
      environment = AccessToken.live(config.token) ++ SlackClient.live(backend)
      _ <- (for {
            resp    <- backend.send(getJoke)
            body    <- IO.fromEither(resp.body)
            joke    <- IO.fromEither(body)
            channel <- randomConversation
            _       <- postChatMessage(channel.id, joke)
          } yield ())
            .repeat(Schedule.fixed(3.hours))
            .toManaged_
            .provideSomeLayer[Clock with Random](environment)
    } yield ()).fold(ex => 1, _ => 0)
}
