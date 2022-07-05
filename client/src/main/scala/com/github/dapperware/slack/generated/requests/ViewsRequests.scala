/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param trigger_id Exchange a trigger to post to the user.
 * @param view A [view payload](/reference/surfaces/views). This must be a JSON-encoded string.
 */
case class OpenViewsRequest(trigger_id: String, view: String)

object OpenViewsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[OpenViewsRequest] = deriveEncoder[OpenViewsRequest]
}

/**
 * @param user_id `id` of the user you want publish a view to.
 * @param view A [view payload](/reference/surfaces/views). This must be a JSON-encoded string.
 * @param hash A string that represents view state to protect against possible race conditions.
 */
case class PublishViewsRequest(user_id: String, view: String, hash: Option[String])

object PublishViewsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[PublishViewsRequest] = deriveEncoder[PublishViewsRequest]
}

/**
 * @param trigger_id Exchange a trigger to post to the user.
 * @param view A [view payload](/reference/surfaces/views). This must be a JSON-encoded string.
 */
case class PushViewsRequest(trigger_id: String, view: String)

object PushViewsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[PushViewsRequest] = deriveEncoder[PushViewsRequest]
}

/**
 * @param view_id A unique identifier of the view to be updated. Either `view_id` or `external_id` is required.
 * @param external_id A unique identifier of the view set by the developer. Must be unique for all views on a team. Max length of 255 characters. Either `view_id` or `external_id` is required.
 * @param view A [view object](/reference/surfaces/views). This must be a JSON-encoded string.
 * @param hash A string that represents view state to protect against possible race conditions.
 */
case class UpdateViewsRequest(
  view_id: Option[String],
  external_id: Option[String],
  view: Option[String],
  hash: Option[String]
)

object UpdateViewsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[UpdateViewsRequest] = deriveEncoder[UpdateViewsRequest]
}
