/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param channel ID of conversation to archive
 */
case class ArchiveConversationsRequest(channel: Option[String])

object ArchiveConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[ArchiveConversationsRequest] = deriveEncoder[ArchiveConversationsRequest]
}

/**
 * @param channel Conversation to close.
 */
case class CloseConversationsRequest(channel: Option[String])

object CloseConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[CloseConversationsRequest] = deriveEncoder[CloseConversationsRequest]
}

/**
 * @param name Name of the public or private channel to create
 * @param is_private Create a private channel instead of a public one
 */
case class CreateConversationsRequest(name: Option[String], is_private: Option[Boolean])

object CreateConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[CreateConversationsRequest] = deriveEncoder[CreateConversationsRequest]
}

/**
 * @param channel Conversation ID to fetch history for.
 * @param latest End of time range of messages to include in results.
 * @param oldest Start of time range of messages to include in results.
 * @param inclusive Include messages with latest or oldest timestamp in results only when either timestamp is specified.
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the users list hasn't been reached.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class HistoryConversationsRequest(
  channel: Option[String],
  latest: Option[String],
  oldest: Option[String],
  inclusive: Option[Boolean],
  limit: Option[Int],
  cursor: Option[String]
)

object HistoryConversationsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[HistoryConversationsRequest] =
    FormEncoder.fromParams.contramap[HistoryConversationsRequest] { req =>
      List(
        "channel"   -> req.channel,
        "latest"    -> req.latest,
        "oldest"    -> req.oldest,
        "inclusive" -> req.inclusive,
        "limit"     -> req.limit,
        "cursor"    -> req.cursor
      )
    }
}

/**
 * @param channel Conversation ID to learn more about
 * @param include_locale Set this to `true` to receive the locale for this conversation. Defaults to `false`
 * @param include_num_members Set to `true` to include the member count for the specified conversation. Defaults to `false`
 */
case class InfoConversationsRequest(
  channel: Option[String],
  include_locale: Option[Boolean],
  include_num_members: Option[Boolean]
)

object InfoConversationsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoConversationsRequest] =
    FormEncoder.fromParams.contramap[InfoConversationsRequest] { req =>
      List(
        "channel"             -> req.channel,
        "include_locale"      -> req.include_locale,
        "include_num_members" -> req.include_num_members
      )
    }
}

/**
 * @param channel The ID of the public or private channel to invite user(s) to.
 * @param users A comma separated list of user IDs. Up to 1000 users may be listed.
 */
case class InviteConversationsRequest(channel: Option[String], users: Option[String])

object InviteConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[InviteConversationsRequest] = deriveEncoder[InviteConversationsRequest]
}

/**
 * @param channel ID of conversation to join
 */
case class JoinConversationsRequest(channel: Option[String])

object JoinConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[JoinConversationsRequest] = deriveEncoder[JoinConversationsRequest]
}

/**
 * @param channel ID of conversation to remove user from.
 * @param user User ID to be removed.
 */
case class KickConversationsRequest(channel: Option[String], user: Option[String])

object KickConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[KickConversationsRequest] = deriveEncoder[KickConversationsRequest]
}

/**
 * @param channel Conversation to leave
 */
case class LeaveConversationsRequest(channel: Option[String])

object LeaveConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[LeaveConversationsRequest] = deriveEncoder[LeaveConversationsRequest]
}

/**
 * @param exclude_archived Set to `true` to exclude archived channels from the list
 * @param types Mix and match channel types by providing a comma-separated list of any combination of `public_channel`, `private_channel`, `mpim`, `im`
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the list hasn't been reached. Must be an integer no larger than 1000.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class ListConversationsRequest(
  exclude_archived: Option[Boolean],
  types: Option[String],
  limit: Option[Int],
  cursor: Option[String]
)

object ListConversationsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListConversationsRequest] =
    FormEncoder.fromParams.contramap[ListConversationsRequest] { req =>
      List(
        "exclude_archived" -> req.exclude_archived,
        "types"            -> req.types,
        "limit"            -> req.limit,
        "cursor"           -> req.cursor
      )
    }
}

/**
 * @param channel Channel or conversation to set the read cursor for.
 * @param ts Unique identifier of message you want marked as most recently seen in this conversation.
 */
case class MarkConversationsRequest(channel: Option[String], ts: Option[Int])

object MarkConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[MarkConversationsRequest] = deriveEncoder[MarkConversationsRequest]
}

/**
 * @param channel ID of the conversation to retrieve members for
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the users list hasn't been reached.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class MembersConversationsRequest(channel: Option[String], limit: Option[Int], cursor: Option[String])

object MembersConversationsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[MembersConversationsRequest] =
    FormEncoder.fromParams.contramap[MembersConversationsRequest] { req =>
      List("channel" -> req.channel, "limit" -> req.limit, "cursor" -> req.cursor)
    }
}

/**
 * @param channel Resume a conversation by supplying an `im` or `mpim`'s ID. Or provide the `users` field instead.
 * @param users Comma separated lists of users. If only one user is included, this creates a 1:1 DM.  The ordering of the users is preserved whenever a multi-person direct message is returned. Supply a `channel` when not supplying `users`.
 * @param return_im Boolean, indicates you want the full IM channel definition in the response.
 */
case class OpenConversationsRequest(channel: Option[String], users: Option[String], return_im: Option[Boolean])

object OpenConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[OpenConversationsRequest] = deriveEncoder[OpenConversationsRequest]
}

/**
 * @param channel ID of conversation to rename
 * @param name New name for conversation.
 */
case class RenameConversationsRequest(channel: Option[String], name: Option[String])

object RenameConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[RenameConversationsRequest] = deriveEncoder[RenameConversationsRequest]
}

/**
 * @param channel Conversation ID to fetch thread from.
 * @param ts Unique identifier of a thread's parent message. `ts` must be the timestamp of an existing message with 0 or more replies. If there are no replies then just the single message referenced by `ts` will return - it is just an ordinary, unthreaded message.
 * @param latest End of time range of messages to include in results.
 * @param oldest Start of time range of messages to include in results.
 * @param inclusive Include messages with latest or oldest timestamp in results only when either timestamp is specified.
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the users list hasn't been reached.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class RepliesConversationsRequest(
  channel: Option[String],
  ts: Option[Int],
  latest: Option[Int],
  oldest: Option[Int],
  inclusive: Option[Boolean],
  limit: Option[Int],
  cursor: Option[String]
)

object RepliesConversationsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RepliesConversationsRequest] =
    FormEncoder.fromParams.contramap[RepliesConversationsRequest] { req =>
      List(
        "channel"   -> req.channel,
        "ts"        -> req.ts,
        "latest"    -> req.latest,
        "oldest"    -> req.oldest,
        "inclusive" -> req.inclusive,
        "limit"     -> req.limit,
        "cursor"    -> req.cursor
      )
    }
}

/**
 * @param channel Conversation to set the purpose of
 * @param purpose A new, specialer purpose
 */
case class SetPurposeConversationsRequest(channel: Option[String], purpose: Option[String])

object SetPurposeConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[SetPurposeConversationsRequest] = deriveEncoder[SetPurposeConversationsRequest]
}

/**
 * @param channel Conversation to set the topic of
 * @param topic The new topic string. Does not support formatting or linkification.
 */
case class SetTopicConversationsRequest(channel: Option[String], topic: Option[String])

object SetTopicConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[SetTopicConversationsRequest] = deriveEncoder[SetTopicConversationsRequest]
}

/**
 * @param channel ID of conversation to unarchive
 */
case class UnarchiveConversationsRequest(channel: Option[String])

object UnarchiveConversationsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[UnarchiveConversationsRequest] = deriveEncoder[UnarchiveConversationsRequest]
}
