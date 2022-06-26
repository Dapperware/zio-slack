/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class OpenViewsResponse(view: com.github.dapperware.slack.models.View)

object OpenViewsResponse {
  implicit val decoder: io.circe.Decoder[OpenViewsResponse] = io.circe.generic.semiauto.deriveDecoder[OpenViewsResponse]
}

case class PublishViewsResponse(view: com.github.dapperware.slack.models.View)

object PublishViewsResponse {
  implicit val decoder: io.circe.Decoder[PublishViewsResponse] =
    io.circe.generic.semiauto.deriveDecoder[PublishViewsResponse]
}

case class PushViewsResponse(view: com.github.dapperware.slack.models.View)

object PushViewsResponse {
  implicit val decoder: io.circe.Decoder[PushViewsResponse] = io.circe.generic.semiauto.deriveDecoder[PushViewsResponse]
}

case class UpdateViewsResponse(view: com.github.dapperware.slack.models.View)

object UpdateViewsResponse {
  implicit val decoder: io.circe.Decoder[UpdateViewsResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateViewsResponse]
}
