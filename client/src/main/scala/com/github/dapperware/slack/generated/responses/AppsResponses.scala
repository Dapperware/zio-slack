/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListAuthorizationsEventAppsResponse(
  authorization: zio.Chunk[com.github.dapperware.slack.models.Authorization]
)

object ListAuthorizationsEventAppsResponse {
  implicit val decoder: io.circe.Decoder[ListAuthorizationsEventAppsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListAuthorizationsEventAppsResponse]
}

case class ListResourcesPermissionsAppsResponse(
  resources: List[String],
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata]
)

object ListResourcesPermissionsAppsResponse {
  implicit val decoder: io.circe.Decoder[ListResourcesPermissionsAppsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListResourcesPermissionsAppsResponse]
}
