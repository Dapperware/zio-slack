/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param channel Channel to add star to, or channel where the message to add star to was posted (used with `timestamp`).
 * @param file File to add star to.
 * @param file_comment File comment to add star to.
 * @param timestamp Timestamp of the message to add star to.
 */
case class AddStarsRequest(
  channel: Option[String] = None,
  file: Option[String] = None,
  file_comment: Option[String] = None,
  timestamp: Option[String] = None
)

object AddStarsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddStarsRequest] = deriveEncoder[AddStarsRequest]
}

/**
 * @param count undefined
 * @param page undefined
 * @param cursor Parameter for pagination. Set `cursor` equal to the `next_cursor` attribute returned by the previous request's `response_metadata`. This parameter is optional, but pagination is mandatory: the default value simply fetches the first "page" of the collection. See [pagination](/docs/pagination) for more details.
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the list hasn't been reached.
 */
case class ListStarsRequest(
  count: Option[String] = None,
  page: Option[String] = None,
  cursor: Option[String] = None,
  limit: Option[Int] = None
)

object ListStarsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListStarsRequest] = FormEncoder.fromParams.contramap[ListStarsRequest] { req =>
    List("count" -> req.count, "page" -> req.page, "cursor" -> req.cursor, "limit" -> req.limit)
  }
}

/**
 * @param channel Channel to remove star from, or channel where the message to remove star from was posted (used with `timestamp`).
 * @param file File to remove star from.
 * @param file_comment File comment to remove star from.
 * @param timestamp Timestamp of the message to remove star from.
 */
case class RemoveStarsRequest(
  channel: Option[String] = None,
  file: Option[String] = None,
  file_comment: Option[String] = None,
  timestamp: Option[String] = None
)

object RemoveStarsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RemoveStarsRequest] = deriveEncoder[RemoveStarsRequest]
}
