package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class ChannelChunk(
  channels: Seq[Channel],
  response_metadata: Option[ResponseMetadata] = None
)

object ChannelChunk {
  implicit val decoder: Decoder[ChannelChunk] = deriveDecoder[ChannelChunk]
}
