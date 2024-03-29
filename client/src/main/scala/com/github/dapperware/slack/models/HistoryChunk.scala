package com.github.dapperware.slack.models

import io.circe.generic.semiauto._
import io.circe.{ Decoder, Json }

case class HistoryItem(`type`: String, user: String, text: String, ts: String)

object HistoryItem {
  implicit val decoder: Decoder[HistoryItem] = deriveDecoder[HistoryItem]
}

case class HistoryChunk(
  latest: Option[String],
  messages: Seq[HistoryItem],
  pin_count: Int,
  has_more: Boolean,
  response_metadata: Option[ResponseMetadata]
)

object HistoryChunk {
  implicit val decoder: Decoder[HistoryChunk] = deriveDecoder[HistoryChunk]
}

case class FileInfo(file: File, comments: Seq[SlackComment], paging: PagingObject)

object FileInfo {
  implicit val decoder: Decoder[FileInfo] = deriveDecoder[FileInfo]
}

case class FilesResponse(files: Seq[File], paging: PagingObject)

object FilesResponse {
  implicit val decoder: Decoder[FilesResponse] = deriveDecoder[FilesResponse]
}

case class ReactionsResponse(
  items: Seq[Json], // TODO: Parse out each object type w/ reactions
  response_metadata: Option[ResponseMetadata]
)

object ReactionsResponse {
  implicit val decoder: Decoder[ReactionsResponse] = deriveDecoder[ReactionsResponse]
}
