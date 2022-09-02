package com.github.dapperware.slack

import com.github.dapperware.slack.models.events._
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax._
import sttp.capabilities.WebSockets
import sttp.client3.{ asWebSocketAlways, basicRequest, SttpBackend, UriContext }
import sttp.ws.{ WebSocket, WebSocketClosed }
import zio.stream.{ ZSink, ZStream }
import zio.{ IO, Promise, Scope, Task, Trace, URLayer, ZIO, ZLayer }

/**
 * A wrapper over the Slack Socket Mode API
 *
 * https://api.slack.com/apis/connections/socket-implement
 */
trait SlackSocket {

  /**
   * Constructs a new slack socket which will stream events from slack.
   *
   * @note that this requires your application to be using "socket mode", and does not provide the reconnect after disconnect
   *       semantics which users will need to implement themselves.
   * @param onMessage Some messages can provide a response during the acknowledgement phase, this function allows you to supply a response
   *                  during that step if you want to immediately update the Slack UI.
   * @param onUnhandled An "escape hatch" for events that couldn't be parsed because of new features added to the Slack API.
   */
  def connect[R](
    onMessage: SocketEventPayload => ZIO[R, Nothing, Option[Json]] = (_: SocketEventPayload) => ZIO.none,
    onUnhandled: Json => ZIO[R, Nothing, Any] = (_: Json) => ZIO.unit
  )(implicit trace: Trace): ZStream[R with AppToken, SlackError, Either[SlackControlEvent, SocketEventPayload]]
}

object SlackSocket {

  def apply[R](
    onMessage: SocketEventPayload => ZIO[R, Nothing, Option[Json]] = (_: SocketEventPayload) => ZIO.none,
    onUnhandled: Json => ZIO[R, Nothing, Any] = (_: Json) => ZIO.unit
  )(implicit
    trace: Trace
  ): ZStream[R with AppToken with SlackSocket, SlackError, Either[SlackControlEvent, SocketEventPayload]] =
    ZStream.serviceWithStream[SlackSocket](
      _.connect(onMessage, onUnhandled)
    )
}

class SlackSocketLive(slack: Slack, client: SttpBackend[Task, WebSockets]) extends SlackSocket {
  private def openWebsocket(implicit trace: Trace): ZIO[Scope with AppToken, SlackError, WebSocket[Task]] = for {
    url  <- slack.openSocketModeConnection.map(_.toEither).absolve
    done <- Promise.make[Nothing, Boolean]
    p    <- Promise.make[Nothing, WebSocket[Task]]
    _    <-
      client
        .send(basicRequest.get(uri"$url").response(asWebSocketAlways[Task, Unit](p.succeed(_) *> done.await.unit)))
        .forkScoped
    ws   <- p.await.withFinalizer(_ => done.succeed(true))
  } yield ws

  def connect[R](
    onMessage: SocketEventPayload => ZIO[R, Nothing, Option[Json]],
    onUnhandled: Json => ZIO[R, Nothing, Any]
  )(implicit trace: Trace): ZStream[R with AppToken, SlackError, Either[SlackControlEvent, SocketEventPayload]] =
    ZStream.unwrapScoped[R with AppToken](for {
      w                <- openWebsocket
      // After the socket has been opened the first message we expect is the "hello" message
      // Chew that off the front of the socket, if we don't receive it we should return an exception
      r                <- readAllMessages(w).peel(ZSink.head[Either[Json, SlackSocketEvent]])
      (handshake, rest) = r
      _                <- ZIO.whenCase(handshake) {
                            case Some(Right(Hello(_, _, _))) => ZIO.unit
                            case _                           =>
                              ZIO.fail(
                                SlackError.CommunicationError("Protocol error: Did not receive hello message from server.", None)
                              )
                          }
      stream            = rest.tap(_.fold(onUnhandled, _ => ZIO.unit)).collectZIO {
                            case Right(c: SlackControlEvent)                                             => ZIO.left[SlackControlEvent](c)
                            case Right(SlackSocketEventEnvelope(id, _, accepts_response, payload, _, _)) =>
                              // Handle the acknowledgement of the message
                              val response =
                                if (accepts_response) onMessage(payload).map(_.map(_.asJson))
                                else ZIO.none

                              response
                                .map(SocketEventAck(id, _).asJson.deepDropNullValues.noSpaces)
                                .flatMap(w.sendText)
                                .mapBoth(SlackError.fromThrowable, _ => Right(payload))
                          }
    } yield stream)

  def parseMessage(message: String)(implicit trace: Trace): IO[io.circe.Error, Either[Json, SlackSocketEvent]] =
    ZIO.fromEither(parse(message).map(json => json.as[SlackSocketEvent].left.map(_ => json)))

  private def readAllMessages(
    ws: WebSocket[Task]
  )(implicit trace: Trace): ZStream[Any, SlackError, Either[Json, SlackSocketEvent]] =
    ZStream
      .repeatZIOOption(readMessage(ws))
      .mapError(SlackError.fromThrowable)

  private def readMessage(
    ws: WebSocket[Task]
  )(implicit trace: Trace): IO[Option[Throwable], Either[Json, SlackSocketEvent]] =
    ws.receiveText()
      .flatMap(parseMessage)
      .asSomeError
      .catchSome { case Some(WebSocketClosed(Some(_))) =>
        ZIO.fail(None)
      }
}

object SlackSocketLive {
  def layer(implicit trace: Trace): URLayer[Slack with SttpBackend[Task, WebSockets], SlackSocket] =
    ZLayer.fromFunction(new SlackSocketLive(_, _))
}
