/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListFilesResponse(files: List[String], paging: com.github.dapperware.slack.models.PagingObject)

object ListFilesResponse {
  implicit val decoder: io.circe.Decoder[ListFilesResponse] = io.circe.generic.semiauto.deriveDecoder[ListFilesResponse]
}

case class AddRemoteFilesResponse(file: com.github.dapperware.slack.models.File)

object AddRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[AddRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[AddRemoteFilesResponse]
}

case class InfoRemoteFilesResponse(file: com.github.dapperware.slack.models.File)

object InfoRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[InfoRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[InfoRemoteFilesResponse]
}

case class ListRemoteFilesResponse(
  files: zio.Chunk[com.github.dapperware.slack.models.File],
  response_metadata: com.github.dapperware.slack.models.ResponseMetadata
)

object ListRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[ListRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListRemoteFilesResponse]
}

case class ShareRemoteFilesResponse(file: com.github.dapperware.slack.models.File)

object ShareRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[ShareRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[ShareRemoteFilesResponse]
}

case class UpdateRemoteFilesResponse(file: com.github.dapperware.slack.models.File)

object UpdateRemoteFilesResponse {
  implicit val decoder: io.circe.Decoder[UpdateRemoteFilesResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateRemoteFilesResponse]
}
