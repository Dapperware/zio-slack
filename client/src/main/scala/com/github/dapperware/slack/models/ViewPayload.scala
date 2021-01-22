package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class View(
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
  notify_on_close: Option[String] = None,
  close: Option[PlainTextObject] = None,
  previous_view_id: Option[String] = None,
  app_id: Option[String] = None,
  external_id: Option[String] = None,
  app_installed_team_id: Option[String] = None,
  bot_id: Option[String] = None
)

object View {
  implicit val viewCodec: Codec[View] = deriveCodec[View]
}

case class ViewPayload(
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

case class ViewState(values: Map[String, Map[String, ViewStateValue]]) {
  def getValue(block: String, action: String): Option[ViewStateValue] =
    values.get(block).flatMap(_.get(action))
}

case class ViewStateValue(`type`: String, value: String)

object ViewState {
  implicit val viewStateChildCodec: Codec.AsObject[ViewStateValue] = deriveCodec[ViewStateValue]

  implicit val viewStateDecoder: Codec.AsObject[ViewState] = deriveCodec[ViewState]
}

object ViewPayload {

  implicit val codec: Codec[ViewPayload] = deriveCodec[ViewPayload]
}
