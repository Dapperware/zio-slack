package com.github.dapperware.slack

import com.github.dapperware.slack.models.{ Hello, OutboundMessage, SlackEvent }
import io.circe.Json
import io.circe.syntax._
import io.circe.parser.parse
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.{ asWebSocketAlways, basicRequest, UriContext }
import sttp.ws.{ WebSocket, WebSocketClosed, WebSocketFrame }
import zio.{ Has, IO, Promise, Task, URLayer, ZManaged }
import zio.stream.{ ZSink, ZStream }

trait SlackSocket {
  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZStream[R with Has[AccessToken], E1, SlackEvent]
}

object SlackSocket {
  def connect[R, E1 >: SlackError](
    outbound: ZStream[R, E1, OutboundMessage]
  ): ZStream[R with Has[AccessToken] with Has[SlackSocket], E1, SlackEvent] =
    ZStream.accessStream[R with Has[AccessToken] with Has[SlackSocket]](
      _.get[SlackSocket].connect(outbound)
    )
}

class SlackSocketLive(slack: Slack, client: SttpClient.Service) extends SlackSocket {
  private def openWebsocket: ZManaged[Has[AccessToken], SlackError, WebSocket[Task]] = for {
    url  <- slack.connectRtm().map(_.toEither).absolve.map(_.url).toManaged_
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
  ): ZStream[R with Has[AccessToken], E1, SlackEvent] =
    ZStream.unwrapManaged(for {
      w                <- openWebsocket
      // After the socket has been opened the first message we expect is the "hello" message
      // Chew that off the front of the socket, if we don't receive it we should return an exception
      r                <- readAllMessages(w).peel(ZSink.head[SlackEvent])
      (handshake, rest) = r
      _                <- ZManaged.whenCase(handshake) {
                            case Some(Hello(_)) => ZManaged.unit
                            case _              =>
                              ZManaged.fail(
                                SlackError.CommunicationError("Protocol error: Did not receive hello message from server.", None)
                              )
                          }
      // Reads the messages being sent from the caller and buffers them while we wait to send them
      // to slack
      _                <- outbound.zipWithIndex.foreachManaged { case (out, idx) =>
                            w.send(WebSocketFrame.text(out.asJson.deepMerge(Json.obj("id" -> idx.asJson)).noSpaces))
                              .mapError(SlackError.fromThrowable)
                          }.fork
    } yield rest)

  def parseMessage(message: String): IO[io.circe.Error, SlackEvent] =
    IO.fromEither(parse(message).flatMap(_.as[SlackEvent]))

  private def readAllMessages(ws: WebSocket[Task]): ZStream[Any, SlackError, SlackEvent] =
    ZStream
      .fromEffectOption(readMessage(ws))
      .mapError(SlackError.fromThrowable)

  private def readMessage(ws: WebSocket[Task]): IO[Option[Throwable], SlackEvent] =
    ws.receiveText()
      .flatMap(parseMessage)
      .asSomeError
      .catchSome { case Some(WebSocketClosed(Some(_))) =>
        IO.fail(None)
      }
}

object SlackSocketLive {
  def layer: URLayer[Has[Slack] with Has[SttpClient.Service], Has[SlackSocket]] =
    (new SlackSocketLive(_, _)).toLayer[SlackSocket]
}
