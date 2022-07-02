/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class CloseConversationsResponse(already_closed: Option[Boolean], no_op: Option[Boolean])

object CloseConversationsResponse {
  implicit val decoder: io.circe.Decoder[CloseConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[CloseConversationsResponse]
}

case class CreateConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object CreateConversationsResponse {
  implicit val decoder: io.circe.Decoder[CreateConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[CreateConversationsResponse]
}

case class HistoryConversationsResponse(
  latest: Option[String] = None,
  oldest: Option[String] = None,
  pin_count: Option[Int] = None,
  has_more: Option[Boolean] = None,
  channel_actions_ts: Option[String] = None,
  channel_actions_count: Option[Int] = None,
  messages: zio.Chunk[com.github.dapperware.slack.models.Message],
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata] = None
)

object HistoryConversationsResponse {
  implicit val decoder: io.circe.Decoder[HistoryConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[HistoryConversationsResponse]
}

case class InfoConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object InfoConversationsResponse {
  implicit val decoder: io.circe.Decoder[InfoConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[InfoConversationsResponse]
}

case class InviteConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object InviteConversationsResponse {
  implicit val decoder: io.circe.Decoder[InviteConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[InviteConversationsResponse]
}

case class JoinConversationsResponse(
  channel: com.github.dapperware.slack.models.Channel,
  response_metadata: Option[com.github.dapperware.slack.models.ResponseMetadata],
  warning: Option[String]
)

object JoinConversationsResponse {
  implicit val decoder: io.circe.Decoder[JoinConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[JoinConversationsResponse]
}

case class LeaveConversationsResponse(not_in_channel: Option[Boolean])

object LeaveConversationsResponse {
  implicit val decoder: io.circe.Decoder[LeaveConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[LeaveConversationsResponse]
}

case class ListConversationsResponse(
  channels: zio.Chunk[com.github.dapperware.slack.models.Channel],
  response_metadata: com.github.dapperware.slack.models.ResponseMetadata
)

object ListConversationsResponse {
  implicit val decoder: io.circe.Decoder[ListConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListConversationsResponse]
}

case class MembersConversationsResponse(
  members: List[String],
  response_metadata: com.github.dapperware.slack.models.ResponseMetadata
)

object MembersConversationsResponse {
  implicit val decoder: io.circe.Decoder[MembersConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[MembersConversationsResponse]
}

case class OpenConversationsResponse(
  already_open: Option[Boolean],
  channel: com.github.dapperware.slack.models.Channel,
  no_op: Option[Boolean]
)

object OpenConversationsResponse {
  implicit val decoder: io.circe.Decoder[OpenConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[OpenConversationsResponse]
}

case class RenameConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object RenameConversationsResponse {
  implicit val decoder: io.circe.Decoder[RenameConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[RenameConversationsResponse]
}

case class RepliesConversationsResponse(has_more: Option[Boolean], messages: List[String])

object RepliesConversationsResponse {
  implicit val decoder: io.circe.Decoder[RepliesConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[RepliesConversationsResponse]
}

case class SetPurposeConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object SetPurposeConversationsResponse {
  implicit val decoder: io.circe.Decoder[SetPurposeConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[SetPurposeConversationsResponse]
}

case class SetTopicConversationsResponse(channel: com.github.dapperware.slack.models.Channel)

object SetTopicConversationsResponse {
  implicit val decoder: io.circe.Decoder[SetTopicConversationsResponse] =
    io.circe.generic.semiauto.deriveDecoder[SetTopicConversationsResponse]
}
