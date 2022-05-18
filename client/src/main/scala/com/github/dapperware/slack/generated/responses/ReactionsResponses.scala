/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListReactionsResponse(
  items: List[String],
  paging: Option[com.github.dapperware.slack.models.PagingObject] = None,
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata] = None
)

object ListReactionsResponse {
  implicit val decoder: io.circe.Decoder[ListReactionsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListReactionsResponse]
}
