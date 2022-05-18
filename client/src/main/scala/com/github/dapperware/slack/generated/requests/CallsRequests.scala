/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param external_unique_id An ID supplied by the 3rd-party Call provider. It must be unique across all Calls from that service.
 * @param join_url The URL required for a client to join the Call.
 * @param external_display_id An optional, human-readable ID supplied by the 3rd-party Call provider. If supplied, this ID will be displayed in the Call object.
 * @param desktop_app_join_url When supplied, available Slack clients will attempt to directly launch the 3rd-party Call with this URL.
 * @param date_start Call start time in UTC UNIX timestamp format
 * @param title The name of the Call.
 * @param created_by The valid Slack user ID of the user who created this Call. When this method is called with a user token, the `created_by` field is optional and defaults to the authed user of the token. Otherwise, the field is required.
 * @param users The list of users to register as participants in the Call. [Read more on how to specify users here](/apis/calls#users).
 */
case class AddCallsRequest(
  external_unique_id: String,
  join_url: String,
  external_display_id: Option[String] = None,
  desktop_app_join_url: Option[String] = None,
  date_start: Option[Int] = None,
  title: Option[String] = None,
  created_by: Option[String] = None,
  users: Option[String] = None
)

object AddCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddCallsRequest] = deriveEncoder[AddCallsRequest]
}

/**
 * @param id `id` returned when registering the call using the [`calls.add`](/methods/calls.add) method.
 * @param duration Call duration in seconds
 */
case class EndCallsRequest(id: String, duration: Option[Int] = None)

object EndCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[EndCallsRequest] = deriveEncoder[EndCallsRequest]
}

/**
 * @param id `id` of the Call returned by the [`calls.add`](/methods/calls.add) method.
 */
case class InfoCallsRequest(id: String)

object InfoCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[InfoCallsRequest] = deriveEncoder[InfoCallsRequest]
}

/**
 * @param id `id` returned by the [`calls.add`](/methods/calls.add) method.
 * @param users The list of users to add as participants in the Call. [Read more on how to specify users here](/apis/calls#users).
 */
case class AddParticipantsCallsRequest(id: String, users: String)

object AddParticipantsCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddParticipantsCallsRequest] = deriveEncoder[AddParticipantsCallsRequest]
}

/**
 * @param id `id` returned by the [`calls.add`](/methods/calls.add) method.
 * @param users The list of users to remove as participants in the Call. [Read more on how to specify users here](/apis/calls#users).
 */
case class RemoveParticipantsCallsRequest(id: String, users: String)

object RemoveParticipantsCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RemoveParticipantsCallsRequest] = deriveEncoder[RemoveParticipantsCallsRequest]
}

/**
 * @param id `id` returned by the [`calls.add`](/methods/calls.add) method.
 * @param title The name of the Call.
 * @param join_url The URL required for a client to join the Call.
 * @param desktop_app_join_url When supplied, available Slack clients will attempt to directly launch the 3rd-party Call with this URL.
 */
case class UpdateCallsRequest(
  id: String,
  title: Option[String] = None,
  join_url: Option[String] = None,
  desktop_app_join_url: Option[String] = None
)

object UpdateCallsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[UpdateCallsRequest] = deriveEncoder[UpdateCallsRequest]
}
