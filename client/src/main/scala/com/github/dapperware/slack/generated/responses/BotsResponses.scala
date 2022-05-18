/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class InfoBotsResponse(bot: com.github.dapperware.slack.models.Bot)

object InfoBotsResponse {
  implicit val decoder: io.circe.Decoder[InfoBotsResponse] = io.circe.generic.semiauto.deriveDecoder[InfoBotsResponse]
}
