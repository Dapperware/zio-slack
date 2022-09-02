package com.github.dapperware.slack.models.events

import cats.implicits.toFunctorOps
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder, Json }

sealed trait SlackSocketEvent

object SlackSocketEvent {
  private val typeDecoder = Decoder.decodeString.at("type")

  implicit val decoder: Decoder[SlackSocketEvent] = typeDecoder.flatMap {
    case "hello"      => Hello.decoder.widen[SlackSocketEvent]
    case "disconnect" => Disconnect.decoder.widen[SlackSocketEvent]
    case _            => SlackSocketEventEnvelope.decoder.widen[SlackSocketEvent]
  }
}

case class ConnectionInfo(
  app_id: String
)

object ConnectionInfo {
  implicit val decoder: Decoder[ConnectionInfo] = deriveDecoder[ConnectionInfo]
}

case class DebugInfo(
  host: String,
  started: Option[String],
  build_number: Option[Int],
  approximate_connection_time: Option[Int]
)

object DebugInfo {
  implicit val decoder: Decoder[DebugInfo] = deriveDecoder[DebugInfo]
}

sealed trait SlackControlEvent

case class Hello(
  connection_info: ConnectionInfo,
  num_connections: Int,
  debug_info: DebugInfo
) extends SlackSocketEvent
    with SlackControlEvent

object Hello {
  implicit val decoder: Decoder[Hello] = deriveDecoder[Hello]
}

case class Disconnect(
  reason: String,
  debug_info: DebugInfo
) extends SlackSocketEvent
    with SlackControlEvent

object Disconnect {
  implicit val decoder: Decoder[Disconnect] = deriveDecoder[Disconnect]
}

case class SlackSocketEventEnvelope(
  envelope_id: String,
  `type`: String,
  accepts_response_payload: Boolean,
  payload: SocketEventPayload,
  retry_attempt: Option[Int],
  retry_reason: Option[String]
) extends SlackSocketEvent

object SlackSocketEventEnvelope {
  private val typeDecoder = Decoder.decodeString.at("type")

  implicit val decoder: Decoder[SlackSocketEventEnvelope] = typeDecoder.flatMap { typ =>
    implicit val payloadDecoder: Decoder[SocketEventPayload] = SocketEventPayload.decoder(typ)
    deriveDecoder[SlackSocketEventEnvelope]
  }
}

sealed trait SocketEventPayload

object SocketEventPayload {
  case class SlashCommand(
    token: String,
    team_id: String,
    team_domain: String,
    channel_id: String,
    channel_name: String,
    user_id: String,
    user_name: String,
    command: String,
    text: String,
    response_url: String,
    trigger_id: String,
    api_app_id: Option[String],
    is_enterprise_install: Option[String]
  ) extends SocketEventPayload

  object SlashCommand {
    implicit val decoder: Decoder[SlashCommand] = deriveDecoder[SlashCommand]
  }

  case class Event(
    token: String,
    team_id: String,
    api_app_id: Option[String],
    event: SlackEvent,
    `type`: String,
    event_id: String,
    event_time: Long,
    is_ext_shared_channel: Option[Boolean],
    event_context: Option[String]
  ) extends SocketEventPayload

  object Event {
    implicit val decoder: Decoder[Event] = deriveDecoder[Event]
  }

  case class Interactive(
  ) extends SocketEventPayload

  object Interactive {
    implicit val decoder: Decoder[Interactive] = deriveDecoder[Interactive]
  }

  def decoder(`type`: String): Decoder[SocketEventPayload] = `type` match {
    case "slash_commands" => SlashCommand.decoder.widen[SocketEventPayload]
    case "events_api"     => Event.decoder.widen[SocketEventPayload]
    case "interactive"    => Interactive.decoder.widen[SocketEventPayload]
    case _                => Decoder.failedWithMessage(s"Unknown SocketEventPayload type: ${`type`}")
  }

}

case class SocketEventAck(
  envelope_id: String,
  payload: Option[Json]
)

object SocketEventAck {
  implicit val encoder: Encoder[SocketEventAck] = deriveEncoder[SocketEventAck]
}
