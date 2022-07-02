package com.github.dapperware.slack.models

import cats.implicits.toFunctorOps
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

sealed trait PinnedItem {
  def `type`: String
  def channel: String
  def comment: Option[String]
  def created_by: String
  def created: Int
}

object PinnedItem {
  case class MessageItem(
    `type`: String,
    channel: String,
    comment: Option[String],
    created_by: String,
    created: Int,
    message: Message
  ) extends PinnedItem

  object MessageItem {
    implicit val decoder: Decoder[MessageItem] = deriveDecoder[MessageItem]
  }

  case class FileItem(
    `type`: String,
    channel: String,
    comment: Option[String],
    created_by: String,
    created: Int,
    file: File
  ) extends PinnedItem

  object FileItem {
    implicit val decoder: Decoder[FileItem] = deriveDecoder[FileItem]
  }

  implicit val decoder: Decoder[PinnedItem] = Decoder[String].at("type").flatMap[PinnedItem] {
    case "message" => MessageItem.decoder.widen[PinnedItem]
    case "file"    => FileItem.decoder.widen[PinnedItem]
    case _         => Decoder.failedWithMessage("Unknown pinned item type")
  }
}
