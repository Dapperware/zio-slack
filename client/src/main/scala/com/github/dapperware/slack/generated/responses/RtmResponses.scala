/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ConnectRtmResponse(
  url: String,
  self: com.github.dapperware.slack.models.User,
  team: com.github.dapperware.slack.models.Team
)

object ConnectRtmResponse {
  implicit val decoder: io.circe.Decoder[ConnectRtmResponse] =
    io.circe.generic.semiauto.deriveDecoder[ConnectRtmResponse]
}
