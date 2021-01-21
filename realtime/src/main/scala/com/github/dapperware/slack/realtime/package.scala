package com.github.dapperware.slack

import com.github.dapperware.slack.api.{ as, request, sendM }
import com.github.dapperware.slack.realtime.models.{ Hello, OutboundMessage, SlackEvent }
import com.github.dapperware.slack.realtime.models.{ OutboundMessage, SlackEvent }
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax.EncoderOps
import sttp.client._
import sttp.client.asynchttpclient.zio.{ SttpClient, ZioWebSocketHandler }
import sttp.client.ws.WebSocket
import sttp.model.ws.WebSocketFrame
import zio.stream.{ Take, ZStream }
import zio.{ Has, IO, Task, ZIO, ZLayer, ZManaged }

package object realtime {
  type SlackRealtimeClient = Has[SlackRealtimeClient.Service]
  type MessageStream       = ZStream[Any, SlackError, SlackEvent]
  type SlackRealtimeEnv    = SlackEnv with SlackRealtimeClient

  object SlackRealtimeClient {
    trait Service {
      private[slack] def openWebsocket: ZIO[SlackEnv, SlackError, WebSocket[Task]]
    }

    def live: ZLayer[SttpClient, Nothing, SlackRealtimeClient] =
      ZLayer.fromService[SttpClient.Service, SlackRealtimeClient.Service](
        client =>
          new Service {
            private[slack] override def openWebsocket: ZIO[SlackEnv, SlackError, WebSocket[Task]] =
              for {
                url <- sendM(request("rtm.connect")) >>= as[String]("url")
                r <- ZioWebSocketHandler().flatMap { handler =>
                      client.openWebsocket(basicRequest.get(uri"$url"), handler)
                    }
              } yield r.result
        }
      )
  }

  private[slack] def openWebsocket: ZIO[SlackEnv with SlackRealtimeClient, SlackError, WebSocket[Task]] =
    ZIO.accessM[SlackRealtimeClient with SlackEnv](_.get.openWebsocket)

  def parseMessage(message: String): IO[io.circe.Error, SlackEvent] =
    IO.fromEither(parse(message).flatMap { json =>
      for {
        message <- json.as[SlackEvent]
      } yield message
    })

  private def readMessage(ws: WebSocket[Task]): Task[Take[Nothing, SlackEvent]] =
    ws.receiveText()
      .flatMap(_.fold(_ => ZIO.succeed(Take.end), value => parseMessage(value).map(Take.single)))

  private val openAndHandshake: ZIO[SlackRealtimeEnv, SlackError, WebSocket[Task]] = for {
    ws <- openWebsocket
    // After the socket has been opened the first message we expect is the "hello" message
    // Chew that off the front of the socket, if we don't receive it we should return an exception
    _ <- readMessage(ws).filterOrFail(
          _.fold(false, _ => false, _.headOption.collectFirst { case Hello(_) => true }.getOrElse(false))
        )(SlackException.ProtocolError("Protocol error did not receive hello as first message"))
  } yield ws

  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZManaged[R with SlackRealtimeEnv, E1, MessageStream] =
    for {
      ws <- openAndHandshake.toManaged_
      // Reads the messages being sent from the caller and buffers them while we wait to send them
      // to slack
      _ <- (for {
            queue <- outbound.toQueueUnbounded
            _ <- ZStream.fromQueue(queue).forever.flattenTake.zipWithIndex.foreachManaged {
                  case (ev, idx) =>
                    ws.send(WebSocketFrame.text(ev.asJson.deepMerge(Json.obj("id" -> idx.asJson)).noSpaces))

                }
          } yield ()).fork
      // We set up the stream to begin receiving text messages
      // We map each of the events into a Take to model the possible end of the stream
      receive = ZStream
        .fromEffect(readMessage(ws))
        .forever
        .flattenTake
    } yield receive
}
