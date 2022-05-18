/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListStarsResponse(
  items: List[String],
  paging: Option[com.github.dapperware.slack.models.PagingObject] = None
)

object ListStarsResponse {
  implicit val decoder: io.circe.Decoder[ListStarsResponse] = io.circe.generic.semiauto.deriveDecoder[ListStarsResponse]
}
