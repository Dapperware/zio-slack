/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListFilesResponse(files: List[String], paging: com.github.dapperware.slack.models.PagingObject)

object ListFilesResponse {
  implicit val decoder: io.circe.Decoder[ListFilesResponse] = io.circe.generic.semiauto.deriveDecoder[ListFilesResponse]
}

case class ListRemoteFilesResponse(
  files: zio.Chunk[com.github.dapperware.slack.models.File],
  response_metadata: com.github.dapperware.slack.models.ResponseMetadata
)

object ListRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[ListRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListRemoteFilesResponse]
}
