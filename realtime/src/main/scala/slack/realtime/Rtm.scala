package slack.realtime

import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import slack.realtime.models.{ Hello, OutboundMessage, SlackEvent }
import slack.{ SlackError, SlackException }
import sttp.client.ws.WebSocket
import sttp.model.ws.WebSocketFrame
import zio._
import zio.stream.{ Take, ZStream }

object Rtm {
  type MessageStream = ZStream[Any, SlackError, SlackEvent]

  def parseMessage(message: String): IO[io.circe.Error, SlackEvent] =
    IO.fromEither(parse(message).flatMap { json =>
      for {
        message <- json.as[SlackEvent]
      } yield message
    })

  trait Service {

    private def readMessage(ws: WebSocket[Task]): Task[Take[Nothing, SlackEvent]] =
      ws.receiveText().flatMap(_.fold(_ => ZIO.succeed(Take.End), value => parseMessage(value).map(Take.Value(_))))

    private val openAndHandshake: ZIO[SlackRealtimeEnv, SlackError, WebSocket[Task]] = for {
      ws <- openWebsocket
      // After the socket has been opened the first message we expect is the "hello" message
      // Chew that off the front of the socket, if we don't receive it we should return an exception
      _ <- readMessage(ws).filterOrFail {
            case Take.Value(Hello(_)) => true
            case _                    => false
          }(SlackException.ProtocolError("Protocol error did not receive hello as first message"))
    } yield ws

    def connect[R, E1 >: SlackError](
      outbound: ZStream[R, E1, OutboundMessage]
    ): ZManaged[R with SlackRealtimeEnv, E1, MessageStream] =
      for {
        ws <- openAndHandshake.toManaged_
        // Reads the messages being sent from the caller and buffers them while we wait to send them
        // to slack
        _ <- (for {
              queue <- outbound.toQueueUnbounded[E1, OutboundMessage]
              _ <- ZStream.fromQueue(queue).forever.unTake.zipWithIndex.foreachManaged {
                    case (event, idx) =>
                      ws.send(WebSocketFrame.text(event.asJson.deepMerge(Json.obj("id" -> idx.asJson)).noSpaces))
                  }
            } yield ()).fork
        // We set up the stream to begin receiving text messages
        // We map each of the events into a Take to model the possible end of the stream
        receive = ZStream
          .fromEffect(readMessage(ws))
          .forever
          .unTake
      } yield receive

  }
}
