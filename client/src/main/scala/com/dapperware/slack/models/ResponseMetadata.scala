package com.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class ResponseMetadata(next_cursor: Option[String])

object ResponseMetadata {
  implicit val decoder: Decoder[ResponseMetadata] = deriveDecoder[ResponseMetadata]
}
