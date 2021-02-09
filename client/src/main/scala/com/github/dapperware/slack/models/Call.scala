package com.github.dapperware.slack.models

import io.circe.{ Codec, Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveCodec, deriveDecoder }
import zio.Chunk
import cats.syntax.applicativeError._
import cats.syntax.functor._
import io.circe.syntax.EncoderOps

import java.time.Instant

sealed trait CallParticipant

object CallParticipant {
  implicit val decoder: Decoder[CallParticipant] =
    Decoder[SlackParticipant].widen[CallParticipant] orElse
      Decoder[ExternalParticipant].widen[CallParticipant]

  implicit val encoder: Encoder[CallParticipant] =
    Encoder.instance {
      case p: SlackParticipant    => p.asJson
      case p: ExternalParticipant => p.asJson
    }
}

case class SlackParticipant(slack_id: String) extends CallParticipant

object SlackParticipant {
  implicit val codec: Codec[SlackParticipant] = deriveCodec[SlackParticipant]
}

case class ExternalParticipant(
  external_id: String,
  display_name: Option[String] = None,
  avatar_url: Option[String] = None
) extends CallParticipant

object ExternalParticipant {
  implicit val codec: Codec[ExternalParticipant] = deriveCodec[ExternalParticipant]
}

case class Call(
  id: String,
  date_start: Instant,
  external_unique_id: String,
  join_url: String,
  desktop_app_join_url: String,
  external_display_id: String,
  title: String,
  users: Chunk[CallParticipant]
)

object Call {
  import io.circe.zio._
  implicit val decoder: Decoder[Call] = deriveDecoder[Call]
}
