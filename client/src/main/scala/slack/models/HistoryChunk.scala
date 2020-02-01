package slack.models

import io.circe.generic.semiauto._
import io.circe.{ Decoder, Json }

case class ResponseMetadata(next_cursor: String)

object ResponseMetadata {
  implicit val decoder: Decoder[ResponseMetadata] = deriveDecoder[ResponseMetadata]
}

case class HistoryItem(`type`: String, user: String, text: String, ts: String)

object HistoryItem {
  implicit val decoder: Decoder[HistoryItem] = deriveDecoder[HistoryItem]
}

case class HistoryChunk(latest: Option[String],
                        messages: Seq[HistoryItem],
                        pin_count: Int,
                        has_more: Boolean,
                        response_metadata: Option[ResponseMetadata])

object HistoryChunk {
  implicit val decoder: Decoder[HistoryChunk] = deriveDecoder[HistoryChunk]
}

case class RepliesChunk(has_more: Boolean, messages: Seq[Json], ok: Boolean)

object RepliesChunk {
  implicit val decoder: Decoder[RepliesChunk] = deriveDecoder[RepliesChunk]
}

case class FileInfo(file: SlackFile, comments: Seq[SlackComment], paging: PagingObject)

object FileInfo {
  implicit val decoder: Decoder[FileInfo] = deriveDecoder[FileInfo]
}

case class FilesResponse(files: Seq[SlackFile], paging: PagingObject)

object FilesResponse {
  implicit val decoder: Decoder[FilesResponse] = deriveDecoder[FilesResponse]
}

case class ReactionsResponse(items: Seq[Json], // TODO: Parse out each object type w/ reactions
                             paging: PagingObject)

object ReactionsResponse {
  implicit val decoder: Decoder[ReactionsResponse] = deriveDecoder[ReactionsResponse]
}

case class PagingObject(count: Int, total: Int, page: Int, pages: Int)

object PagingObject {
  implicit val decoder: Decoder[PagingObject] = deriveDecoder[PagingObject]
}

case class AccessToken(access_token: String, scope: String)

case class RtmStartState(url: String,
                         self: User,
                         team: Team,
                         users: Seq[User],
                         channels: Seq[Channel],
                         groups: Seq[Group],
                         ims: Seq[Im],
                         bots: Seq[Json])
