package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Attachment(
  fallback: Option[String] = None,
  callback_id: Option[String] = None,
  color: Option[String] = None,
  pretext: Option[String] = None,
  blocks: Option[List[Block]] = None,
  author_name: Option[String] = None,
  author_link: Option[String] = None,
  author_icon: Option[String] = None,
  title: Option[String] = None,
  title_link: Option[String] = None,
  text: Option[String] = None,
  fields: Option[Seq[AttachmentField]] = None,
  image_url: Option[String] = None,
  thumb_url: Option[String] = None,
  actions: Option[Seq[ActionField]] = None,
  mrkdwn_in: Option[Seq[String]] = None,
  footer: Option[String] = None,
  footer_icon: Option[String] = None,
  ts: Option[Long] = None
)

object Attachment {
  implicit val codec: Codec.AsObject[Attachment] = deriveCodec[Attachment]
}

case class AttachmentField(title: String, value: String, short: Boolean)

object AttachmentField {
  implicit val codec: Codec.AsObject[AttachmentField] = deriveCodec[AttachmentField]
}

case class ActionField(
  name: String,
  text: String,
  `type`: String,
  style: Option[String] = None,
  value: Option[String] = None,
  confirm: Option[ConfirmField] = None
)

object ActionField {
  implicit val codec: Codec.AsObject[ActionField] = deriveCodec[ActionField]
}

case class ConfirmField(
  text: String,
  title: Option[String] = None,
  ok_text: Option[String] = None,
  cancel_text: Option[String] = None
)

object ConfirmField {
  implicit val codec: Codec.AsObject[ConfirmField] = deriveCodec[ConfirmField]
}
