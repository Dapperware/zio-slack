/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ConversationsUsersResponse(
  channels: List[String],
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata]
)

object ConversationsUsersResponse {
  implicit val decoder: io.circe.Decoder[ConversationsUsersResponse] =
    io.circe.generic.semiauto.deriveDecoder[ConversationsUsersResponse]
}

case class GetPresenceUsersResponse(
  auto_away: Option[Boolean],
  connection_count: Option[Int],
  last_activity: Option[Int],
  manual_away: Option[Boolean],
  online: Option[Boolean],
  presence: String
)

object GetPresenceUsersResponse {
  implicit val decoder: io.circe.Decoder[GetPresenceUsersResponse] =
    io.circe.generic.semiauto.deriveDecoder[GetPresenceUsersResponse]
}

case class InfoUsersResponse(user: com.github.dapperware.slack.models.User)

object InfoUsersResponse {
  implicit val decoder: io.circe.Decoder[InfoUsersResponse] = io.circe.generic.semiauto.deriveDecoder[InfoUsersResponse]
}

case class ListUsersResponse(
  cache_ts: Int,
  members: List[String],
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata]
)

object ListUsersResponse {
  implicit val decoder: io.circe.Decoder[ListUsersResponse] = io.circe.generic.semiauto.deriveDecoder[ListUsersResponse]
}

case class LookupByEmailUsersResponse(user: com.github.dapperware.slack.models.User)

object LookupByEmailUsersResponse {
  implicit val decoder: io.circe.Decoder[LookupByEmailUsersResponse] =
    io.circe.generic.semiauto.deriveDecoder[LookupByEmailUsersResponse]
}
