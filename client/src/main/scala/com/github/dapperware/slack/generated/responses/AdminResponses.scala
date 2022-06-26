/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class CreateConversationsAdminResponse(channel_id: Option[String])

object CreateConversationsAdminResponse {
  implicit val decoder: io.circe.Decoder[CreateConversationsAdminResponse] =
    io.circe.generic.semiauto.deriveDecoder[CreateConversationsAdminResponse]
}

case class GetTeamsConversationsAdminResponse(
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata],
  team_ids: List[String]
)

object GetTeamsConversationsAdminResponse {
  implicit val decoder: io.circe.Decoder[GetTeamsConversationsAdminResponse] =
    io.circe.generic.semiauto.deriveDecoder[GetTeamsConversationsAdminResponse]
}

case class SearchConversationsAdminResponse(channels: List[String], next_cursor: String)

object SearchConversationsAdminResponse {
  implicit val decoder: io.circe.Decoder[SearchConversationsAdminResponse] =
    io.circe.generic.semiauto.deriveDecoder[SearchConversationsAdminResponse]
}
