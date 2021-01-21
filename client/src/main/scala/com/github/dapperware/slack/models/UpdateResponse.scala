package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class UpdateResponse(ok: Boolean, channel: String, ts: String, text: String)

object UpdateResponse {
  implicit val decoder: Decoder[UpdateResponse] = deriveDecoder[UpdateResponse]
}
