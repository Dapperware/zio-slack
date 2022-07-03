package com.github.dapperware.slack.models

import io.circe.syntax._
import io.circe.{ Encoder, Json }

sealed trait OutboundMessage

object OutboundMessage {

  implicit val sendMessageEncoder: Encoder.AsObject[SendMessage] = io.circe.generic.semiauto.deriveEncoder[SendMessage]

  implicit val encoder: Encoder[OutboundMessage] = Encoder.instance { case i: SendMessage =>
    i.asJson.deepMerge(Json.obj("type" -> "message".asJson))
  }

}

case class SendMessage(channel: String, text: String, thread_ts: Option[String] = None) extends OutboundMessage
