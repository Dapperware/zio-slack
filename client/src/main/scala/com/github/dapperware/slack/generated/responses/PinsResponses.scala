/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListPinsResponse(items: zio.Chunk[com.github.dapperware.slack.models.PinnedItem])

object ListPinsResponse {
  implicit val decoder: io.circe.Decoder[ListPinsResponse] = io.circe.generic.semiauto.deriveDecoder[ListPinsResponse]
}
