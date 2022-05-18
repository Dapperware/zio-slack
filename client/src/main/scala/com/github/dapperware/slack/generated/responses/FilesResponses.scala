/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListFilesResponse(files: List[String], paging: com.github.dapperware.slack.models.PagingObject)

object ListFilesResponse {
  implicit val decoder: io.circe.Decoder[ListFilesResponse] = io.circe.generic.semiauto.deriveDecoder[ListFilesResponse]
}
