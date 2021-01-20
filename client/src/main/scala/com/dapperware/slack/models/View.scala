package com.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class View(
  `type`: String,
  title: PlainTextObject,
  blocks: Seq[Block],
  close: Option[PlainTextObject] = None,
  submit: Option[PlainTextObject] = None,
  privateMetaData: Option[String] = None,
  callbackId: Option[String] = None,
  clearOnClose: Option[Boolean] = None,
  notifyOnClose: Option[Boolean] = None,
  externalId: Option[String] = None
)

object View {

  implicit val codec: Codec[View] = deriveCodec[View]
}
