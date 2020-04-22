package slack.models

import io.circe.Decoder
import io.circe.generic.semiauto._

case class SearchChunk[T](
  matches: T,
  pagination: Pagination,
  paging: PagingObject
)

object SearchChunk {
  implicit def decoder[T: Decoder]: Decoder[SearchChunk[T]] = deriveDecoder[SearchChunk[T]]
}

case class SearchMessage(
  iid: String,
  permalink: String,
  team: String,
  text: String,
  ts: String,
  `type`: String,
  user: String,
  username: String,
  channel: EmbeddedChannel
)

object SearchMessage {
  implicit val decoder: Decoder[SearchMessage] = deriveDecoder[SearchMessage]
}

case class EmbeddedChannel(
  id: String,
  is_ext_shared: Boolean,
  is_mpim: Boolean,
  is_org_shared: Boolean,
  is_pending_ext_shared: Boolean,
  is_private: Boolean,
  name: String,
  pending_shared: List[String]
)

object EmbeddedChannel {
  implicit val decoder: Decoder[EmbeddedChannel] = deriveDecoder[EmbeddedChannel]
}
