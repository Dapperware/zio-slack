package com.dapperware.slack.models

import io.circe.{ Codec, Decoder, Encoder }
import io.circe.generic.semiauto._

case class View(
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
  submit_disabled: Option[Boolean] = None,
  state: Option[ViewState] = None
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

object View {

  implicit val codec: Codec[View] = deriveCodec[View]
}
