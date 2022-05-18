/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class EndSnoozeDndResponse(
  dnd_enabled: Boolean,
  next_dnd_end_ts: Int,
  next_dnd_start_ts: Int,
  snooze_enabled: Boolean
)

object EndSnoozeDndResponse {
  implicit val decoder: io.circe.Decoder[EndSnoozeDndResponse] =
    io.circe.generic.semiauto.deriveDecoder[EndSnoozeDndResponse]
}

case class InfoDndResponse(
  dnd_enabled: Boolean,
  next_dnd_end_ts: Int,
  next_dnd_start_ts: Int,
  snooze_enabled: Option[Boolean] = None,
  snooze_endtime: Option[Int] = None,
  snooze_remaining: Option[Int] = None
)

object InfoDndResponse {
  implicit val decoder: io.circe.Decoder[InfoDndResponse] = io.circe.generic.semiauto.deriveDecoder[InfoDndResponse]
}

case class SetSnoozeDndResponse(snooze_enabled: Boolean, snooze_endtime: Int, snooze_remaining: Int)

object SetSnoozeDndResponse {
  implicit val decoder: io.circe.Decoder[SetSnoozeDndResponse] =
    io.circe.generic.semiauto.deriveDecoder[SetSnoozeDndResponse]
}
