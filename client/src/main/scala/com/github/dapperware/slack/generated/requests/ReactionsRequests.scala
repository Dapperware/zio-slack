/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param channel Channel where the message to add reaction to was posted.
 * @param name Reaction (emoji) name.
 * @param timestamp Timestamp of the message to add reaction to.
 */
case class AddReactionsRequest(channel: String, name: String, timestamp: String)

object AddReactionsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[AddReactionsRequest] = deriveEncoder[AddReactionsRequest]
}

/**
 * @param channel Channel where the message to get reactions for was posted.
 * @param file File to get reactions for.
 * @param file_comment File comment to get reactions for.
 * @param full If true always return the complete reaction list.
 * @param timestamp Timestamp of the message to get reactions for.
 */
case class GetReactionsRequest(
  channel: Option[String],
  file: Option[String],
  file_comment: Option[String],
  full: Option[Boolean],
  timestamp: Option[String]
)

object GetReactionsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[GetReactionsRequest] = FormEncoder.fromParams.contramap[GetReactionsRequest] {
    req =>
      List(
        "channel"      -> req.channel,
        "file"         -> req.file,
        "file_comment" -> req.file_comment,
        "full"         -> req.full,
        "timestamp"    -> req.timestamp
      )
  }
}

/**
 * @param user Show reactions made by this user. Defaults to the authed user.
 * @param full If true always return the complete reaction list.
 * @param count undefined
 * @param page undefined
 * @param cursor Parameter for pagination. Set `cursor` equal to the `next_cursor` attribute returned by the previous request's `response_metadata`. This parameter is optional, but pagination is mandatory: the default value simply fetches the first "page" of the collection. See [pagination](/docs/pagination) for more details.
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the list hasn't been reached.
 */
case class ListReactionsRequest(
  user: Option[String],
  full: Option[Boolean],
  count: Option[Int],
  page: Option[Int],
  cursor: Option[String],
  limit: Option[Int]
)

object ListReactionsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListReactionsRequest] = FormEncoder.fromParams.contramap[ListReactionsRequest] {
    req =>
      List(
        "user"   -> req.user,
        "full"   -> req.full,
        "count"  -> req.count,
        "page"   -> req.page,
        "cursor" -> req.cursor,
        "limit"  -> req.limit
      )
  }
}

/**
 * @param name Reaction (emoji) name.
 * @param file File to remove reaction from.
 * @param file_comment File comment to remove reaction from.
 * @param channel Channel where the message to remove reaction from was posted.
 * @param timestamp Timestamp of the message to remove reaction from.
 */
case class RemoveReactionsRequest(
  name: String,
  file: Option[String],
  file_comment: Option[String],
  channel: Option[String],
  timestamp: Option[String]
)

object RemoveReactionsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[RemoveReactionsRequest] = deriveEncoder[RemoveReactionsRequest]
}
