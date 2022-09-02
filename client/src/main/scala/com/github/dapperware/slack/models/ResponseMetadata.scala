package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class ResponseMetadata(next_cursor: Option[String])

object ResponseMetadata {
  // response_metadata returns empty string when there are no more pages
  implicit val decoder: Decoder[ResponseMetadata] = deriveDecoder[ResponseMetadata].map { responseMetadata =>
    responseMetadata.copy(
      next_cursor = responseMetadata.next_cursor.filter(_.nonEmpty)
    )
  }
}
