package com.github.dapperware.slack.models

import cats.Applicative.ops.toAllApplicativeOps
import io.circe.{ Codec, Decoder }
import io.circe.generic.semiauto._

final case class View(
  id: String,
  team_id: String,
  `type`: String,
  blocks: List[Block],
  title: PlainTextObject,
  submit: Option[PlainTextObject] = None,
  private_metadata: Option[String] = None,
  callback_id: Option[String] = None,
  state: Option[ViewState] = None,
  hash: Option[String] = None,
  clear_on_close: Option[Boolean] = None,
  notify_on_close: Option[Boolean] = None,
  close: Option[PlainTextObject] = None,
  previous_view_id: Option[String] = None,
  app_id: Option[String] = None,
  external_id: Option[String] = None,
  app_installed_team_id: Option[String] = None,
  bot_id: Option[String] = None
)

object View {
  implicit val viewCodec: Decoder[View] = deriveDecoder[View]
}

final case class ViewPayload(
  `type`: String,
  title: PlainTextObject,
  blocks: Seq[Block],
  close: Option[PlainTextObject] = None,
  submit: Option[PlainTextObject] = None,
  private_meta_data: Option[String] = None,
  callback_id: Option[String] = None,
  clear_on_close: Option[Boolean] = None,
  notify_on_close: Option[Boolean] = None,
  external_id: Option[String] = None,
  submit_disabled: Option[Boolean] = None
)

final case class ViewState(values: Map[String, Map[String, ViewStateValue]]) {
  def getValue(block: String, action: String): Option[ViewStateValue] =
    values.get(block).flatMap(_.get(action))
}

sealed trait ViewStateValue {
  def `type`: String
}

case class PlainTextValue(value: Option[String]) extends ViewStateValue {
  val `type` = "plain_text_input"
}

case class SelectedOption(text: PlainTextObject, value: String)

case class StaticSelectValue(selected_option: Option[SelectedOption]) extends ViewStateValue {
  override val `type`: String = "static_select"
}

case class MultiStaticSelectValue(selected_options: List[SelectedOption]) extends ViewStateValue {
  override val `type`: String = "multi_static_select"
}

case class MultiConversationsValue(selected_conversations: List[String]) extends ViewStateValue {
  override val `type`: String = "multi_conversations_select"
}

case class ConversationsSelectValue(selected_conversation: Option[String]) extends ViewStateValue {
  override val `type`: String = "conversations_select"
}

case class DatepickerValue(selected_date: Option[String]) extends ViewStateValue {
  override val `type`: String = "datepicker"
}

case class MultiUsersSelectValue(selected_users: List[String]) extends ViewStateValue {
  override val `type`: String = "multi_users_select"
}

case class UsersSelectValue(selected_user: Option[String]) extends ViewStateValue {
  override val `type`: String = "users_select"
}

object ViewState {
  private val typeDecoder: Decoder[String]                                               = Decoder.decodeString.at("type")
  private implicit val selectedOptionDecoder: Decoder[SelectedOption]                    = deriveDecoder[SelectedOption]
  private implicit val plainTextDecoder: Decoder[PlainTextValue]                         = deriveDecoder
  private implicit val staticSelectDecoder: Decoder[StaticSelectValue]                   = deriveDecoder
  private implicit val multiStaticSelectDecoder: Decoder[MultiStaticSelectValue]         = deriveDecoder
  private implicit val datepickerDecoder: Decoder[DatepickerValue]                       = deriveDecoder
  private implicit val multiUserSelectDecoder: Decoder[MultiUsersSelectValue]            = deriveDecoder
  private implicit val usersSelectDecoder: Decoder[UsersSelectValue]                     = deriveDecoder
  private implicit val multiConversationsSelectDecoder: Decoder[MultiConversationsValue] = deriveDecoder
  private implicit val conversationsSelectDecoder: Decoder[ConversationsSelectValue]     = deriveDecoder

  implicit val viewStateChildDecoder: Decoder[ViewStateValue] = typeDecoder.flatMap {
    case "plain_text_input"           => Decoder[PlainTextValue].widen
    case "static_select"              => Decoder[StaticSelectValue].widen
    case "multi_static_select"        => Decoder[MultiStaticSelectValue].widen
    case "datepicker"                 => Decoder[DatepickerValue].widen
    case "multi_users_select"         => Decoder[MultiUsersSelectValue].widen
    case "users_select"               => Decoder[UsersSelectValue].widen
    case "multi_conversations_select" => Decoder[MultiConversationsValue].widen
    case "conversations_select"       => Decoder[ConversationsSelectValue].widen
    case t                            => Decoder.failedWithMessage(s"Unknown view type $t is not supported")
  }

  implicit val viewStateDecoder: Decoder[ViewState] = deriveDecoder[ViewState]
}

object ViewPayload {

  implicit val codec: Codec[ViewPayload] = deriveCodec[ViewPayload]
}
