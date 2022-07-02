package com.github.dapperware.slack.realtime

import com.github.dapperware.slack.realtime.models.{ Hello, OutboundMessage, SlackEvent }
import com.github.dapperware.slack.{ AccessToken, Slack, SlackError, SlackException }
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax._
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClient.Service
import sttp.client3.{ asWebSocketAlways, basicRequest, UriContext }
import sttp.ws.{ WebSocket, WebSocketClosed, WebSocketFrame }
import zio.stream.{ Take, ZStream }
import zio.{ Has, IO, Promise, Task, UIO, URLayer, ZManaged }

trait SlackRealtimeClient {

  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZManaged[R with Has[AccessToken], E1, MessageStream]
}

class SlackRealtimeClientImpl(slack: Slack, client: SttpClient.Service) extends SlackRealtimeClient {
  private def openWebsocket: ZManaged[Has[AccessToken], SlackError, WebSocket[Task]] = for {
    url  <- Slack.connectRtm().map(_.toEither).absolve.map(_.url).toManaged_.provideSome[Has[AccessToken]](_.add(slack))
    done <- Promise.makeManaged[Nothing, Boolean]
    p    <- Promise.makeManaged[Nothing, WebSocket[Task]]
    _    <-
      client
        .send(basicRequest.get(uri"$url").response(asWebSocketAlways[Task, Unit](p.succeed(_) *> done.await.unit)))
        .forkManaged
    ws   <- p.await.toManaged(_ => done.succeed(true))
  } yield ws

  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZManaged[R with Has[AccessToken], E1, MessageStream] =
    (for {

      ws     <- openAndHandshake
      // Reads the messages being sent from the caller and buffers them while we wait to send them
      // to slack
      _      <- (for {
                  queue <- outbound.toQueueUnbounded
                  _     <- ZStream.fromQueue(queue).forever.flattenTake.zipWithIndex.foreachManaged { case (ev, idx) =>
                             ws.send(WebSocketFrame.text(ev.asJson.deepMerge(Json.obj("id" -> idx.asJson)).noSpaces))
                               .mapError(SlackError.fromThrowable)

                           }
                } yield ()).fork
      // We set up the stream to begin receiving text messages
      // We map each of the events into a Take to model the possible end of the stream
      receive = ZStream
                  .fromEffect(readMessage(ws))
                  .forever
                  .flattenTake
                  .mapError(SlackError.fromThrowable)
    } yield receive)

  def parseMessage(message: String): IO[io.circe.Error, SlackEvent] =
    IO.fromEither(parse(message).flatMap(_.as[SlackEvent]))

  private def readMessage(ws: WebSocket[Task]): Task[Take[Nothing, SlackEvent]] =
    ws.receiveText()
      .flatMap(parseMessage(_).map(Take.single))
      .catchSome { case WebSocketClosed(Some(_)) =>
        UIO.succeed(Take.end)
      }

  private val openAndHandshake: ZManaged[Has[AccessToken], SlackError, WebSocket[Task]] = for {
    ws <- openWebsocket
    // After the socket has been opened the first message we expect is the "hello" message
    // Chew that off the front of the socket, if we don't receive it we should return an exception
    _  <- readMessage(ws)
            .filterOrFail(
              _.fold(false, _ => false, _.headOption.collectFirst { case Hello(_) => true }.getOrElse(false))
            )(SlackException.ProtocolError("Protocol error did not receive hello as first message"))
            .mapError(SlackError.fromThrowable)
            .toManaged_
  } yield ws
}

object SlackRealtimeClient {
  def live: URLayer[Has[Slack] with Has[Service], Has[SlackRealtimeClient]] =
    (new SlackRealtimeClientImpl(_, _)).toLayer[SlackRealtimeClient]

  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZManaged[R with Has[Slack] with Has[AccessToken] with Has[SlackRealtimeClient], E1, MessageStream] =
    ZManaged.accessManaged[R with Has[Slack] with Has[AccessToken] with Has[SlackRealtimeClient]](
      _.get[SlackRealtimeClient].connect(outbound)
    )

}
