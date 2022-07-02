package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class SnoozeInfo(
  snoozeEnabled: Boolean,
  nextSnoozeStartTs: Long,
  nextSnoozeEndTs: Long
)

object SnoozeInfo {
  implicit val decoder: Decoder[SnoozeInfo] = deriveDecoder[SnoozeInfo]
}
