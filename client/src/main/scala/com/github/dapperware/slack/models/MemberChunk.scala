package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class MemberChunk(members: Seq[String], response_metadata: Option[ResponseMetadata] = None)

object MemberChunk {
  implicit val decoder: Decoder[MemberChunk] = deriveDecoder[MemberChunk]
}
