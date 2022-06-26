/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class AddCallsResponse(call: com.github.dapperware.slack.models.Call)

object AddCallsResponse {
  implicit val decoder: io.circe.Decoder[AddCallsResponse] = io.circe.generic.semiauto.deriveDecoder[AddCallsResponse]
}

case class EndCallsResponse(call: com.github.dapperware.slack.models.Call)

object EndCallsResponse {
  implicit val decoder: io.circe.Decoder[EndCallsResponse] = io.circe.generic.semiauto.deriveDecoder[EndCallsResponse]
}

case class InfoCallsResponse(call: com.github.dapperware.slack.models.Call)

object InfoCallsResponse {
  implicit val decoder: io.circe.Decoder[InfoCallsResponse] = io.circe.generic.semiauto.deriveDecoder[InfoCallsResponse]
}

case class AddParticipantsCallsResponse(call: com.github.dapperware.slack.models.Call)

object AddParticipantsCallsResponse {
  implicit val decoder: io.circe.Decoder[AddParticipantsCallsResponse] =
    io.circe.generic.semiauto.deriveDecoder[AddParticipantsCallsResponse]
}

case class RemoveParticipantsCallsResponse(call: com.github.dapperware.slack.models.Call)

object RemoveParticipantsCallsResponse {
  implicit val decoder: io.circe.Decoder[RemoveParticipantsCallsResponse] =
    io.circe.generic.semiauto.deriveDecoder[RemoveParticipantsCallsResponse]
}

case class UpdateCallsResponse(call: com.github.dapperware.slack.models.Call)

object UpdateCallsResponse {
  implicit val decoder: io.circe.Decoder[UpdateCallsResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateCallsResponse]
}
