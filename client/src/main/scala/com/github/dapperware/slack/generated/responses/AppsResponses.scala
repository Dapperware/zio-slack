/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListResourcesPermissionsAppsResponse(
  resources: List[String],
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata] = None
)

object ListResourcesPermissionsAppsResponse {
  implicit val decoder: io.circe.Decoder[ListResourcesPermissionsAppsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListResourcesPermissionsAppsResponse]
}
