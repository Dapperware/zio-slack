package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class DndInfo(
  dnd_enabled: Boolean,
  next_dnd_start_ts: Long,
  next_dnd_end_ts: Long
)

object DndInfo {
  implicit val decoder: Decoder[DndInfo] = deriveDecoder[DndInfo]
}
