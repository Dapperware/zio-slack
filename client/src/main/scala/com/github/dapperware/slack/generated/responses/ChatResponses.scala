/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class DeleteChatResponse(channel: String, ts: String)

object DeleteChatResponse {
  implicit val decoder: io.circe.Decoder[DeleteChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[DeleteChatResponse]
}

case class GetPermalinkChatResponse(channel: String, permalink: String)

object GetPermalinkChatResponse {
  implicit val decoder: io.circe.Decoder[GetPermalinkChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[GetPermalinkChatResponse]
}

case class MeMessageChatResponse(channel: Option[String], ts: Option[String])

object MeMessageChatResponse {
  implicit val decoder: io.circe.Decoder[MeMessageChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[MeMessageChatResponse]
}

case class PostEphemeralChatResponse(message_ts: String)

object PostEphemeralChatResponse {
  implicit val decoder: io.circe.Decoder[PostEphemeralChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[PostEphemeralChatResponse]
}

case class PostMessageChatResponse(channel: String, message: com.github.dapperware.slack.models.Message, ts: String)

object PostMessageChatResponse {
  implicit val decoder: io.circe.Decoder[PostMessageChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[PostMessageChatResponse]
}

case class ScheduleMessageChatResponse(
  channel: String,
  message: com.github.dapperware.slack.models.Message,
  post_at: Int,
  scheduled_message_id: String
)

object ScheduleMessageChatResponse {
  implicit val decoder: io.circe.Decoder[ScheduleMessageChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[ScheduleMessageChatResponse]
}

case class ListScheduledMessagesChatResponse(
  response_metadata: com.github.dapperware.slack.models.ResponseMetadata,
  scheduled_messages: List[String]
)

object ListScheduledMessagesChatResponse {
  implicit val decoder: io.circe.Decoder[ListScheduledMessagesChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListScheduledMessagesChatResponse]
}

case class UpdateChatResponse(
  channel: String,
  message: com.github.dapperware.slack.models.Message,
  text: String,
  ts: String
)

object UpdateChatResponse {
  implicit val decoder: io.circe.Decoder[UpdateChatResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateChatResponse]
}
