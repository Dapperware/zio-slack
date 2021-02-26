package com.github.dapperware.slack

import com.github.dapperware.slack.api.{ as, request, sendM }
import com.github.dapperware.slack.realtime.models.{ Hello, OutboundMessage, SlackEvent }
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax.EncoderOps
import sttp.client3.asynchttpclient.zio.{ send, SttpClient }
import sttp.client3.{ asWebSocketAlways, basicRequest, UriContext }
import sttp.ws.{ WebSocket, WebSocketClosed, WebSocketFrame }
import zio.stream.{ Take, ZStream }
import zio.{ Has, IO, Promise, Task, UIO, ZLayer, ZManaged }

package object realtime {
  type SlackRealtimeClient = Has[SlackRealtimeClient.Service]
  type MessageStream       = ZStream[Any, SlackError, SlackEvent]
  type SlackRealtimeEnv    = SlackEnv with SlackRealtimeClient

  object SlackRealtimeClient {
    trait Service {
      private[slack] def openWebsocket: ZManaged[SlackEnv, SlackError, WebSocket[Task]]
    }

    def live: ZLayer[SttpClient, Nothing, SlackRealtimeClient] =
      ZLayer.fromFunction[SttpClient, SlackRealtimeClient.Service](client =>
        new Service {
          private[slack] override def openWebsocket: ZManaged[SlackEnv, SlackError, WebSocket[Task]] =
            for {
              url  <- (sendM(request("rtm.connect")) >>= as[String]("url")).toManaged_
              done <- Promise.makeManaged[Nothing, Boolean]
              p    <- Promise.makeManaged[Nothing, WebSocket[Task]]
              _    <- send(basicRequest.get(uri"$url").response(asWebSocketAlways(p.succeed(_) *> done.await.unit)))
                        .provide(client)
                        .forkManaged
              ws   <- p.await.toManaged(_ => done.succeed(true))
            } yield ws
        }
      )
  }

  private[slack] def openWebsocket: ZManaged[SlackEnv with SlackRealtimeClient, SlackError, WebSocket[Task]] =
    ZManaged.accessManaged[SlackRealtimeClient with SlackEnv](_.get.openWebsocket)

  def parseMessage(message: String): IO[io.circe.Error, SlackEvent] =
    IO.fromEither(parse(message).flatMap { json =>
      for {
        message <- json.as[SlackEvent]
      } yield message
    })

  private def readMessage(ws: WebSocket[Task]): Task[Take[Nothing, SlackEvent]] =
    ws.receiveText()
      .flatMap(parseMessage(_).map(Take.single))
      .catchSome { case WebSocketClosed(Some(_)) =>
        UIO.succeed(Take.end)
      }

  private val openAndHandshake: ZManaged[SlackRealtimeEnv, SlackError, WebSocket[Task]] = for {
    ws <- openWebsocket
    // After the socket has been opened the first message we expect is the "hello" message
    // Chew that off the front of the socket, if we don't receive it we should return an exception
    _  <- readMessage(ws)
            .filterOrFail(
              _.fold(false, _ => false, _.headOption.collectFirst { case Hello(_) => true }.getOrElse(false))
            )(SlackException.ProtocolError("Protocol error did not receive hello as first message"))
            .toManaged_
  } yield ws

  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZManaged[R with SlackRealtimeEnv, E1, MessageStream] =
    for {
      ws     <- openAndHandshake
      // Reads the messages being sent from the caller and buffers them while we wait to send them
      // to slack
      _      <- (for {
                  queue <- outbound.toQueueUnbounded
                  _     <- ZStream.fromQueue(queue).forever.flattenTake.zipWithIndex.foreachManaged { case (ev, idx) =>
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
