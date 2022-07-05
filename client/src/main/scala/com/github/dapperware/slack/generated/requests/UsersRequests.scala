/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param user Browse conversations by a specific user ID's membership. Non-public channels are restricted to those where the calling user shares membership.
 * @param types Mix and match channel types by providing a comma-separated list of any combination of `public_channel`, `private_channel`, `mpim`, `im`
 * @param exclude_archived Set to `true` to exclude archived channels from the list
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the list hasn't been reached. Must be an integer no larger than 1000.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class ConversationsUsersRequest(
  user: Option[String],
  types: Option[String],
  exclude_archived: Option[Boolean],
  limit: Option[Int],
  cursor: Option[String]
)

object ConversationsUsersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ConversationsUsersRequest] =
    FormEncoder.fromParams.contramap[ConversationsUsersRequest] { req =>
      List(
        "user"             -> req.user,
        "types"            -> req.types,
        "exclude_archived" -> req.exclude_archived,
        "limit"            -> req.limit,
        "cursor"           -> req.cursor
      )
    }
}

/**
 * @param user User to get presence info on. Defaults to the authed user.
 */
case class GetPresenceUsersRequest(user: Option[String])

object GetPresenceUsersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[GetPresenceUsersRequest] =
    FormEncoder.fromParams.contramap[GetPresenceUsersRequest] { req =>
      List("user" -> req.user)
    }
}

/**
 * @param include_locale Set this to `true` to receive the locale for this user. Defaults to `false`
 * @param user User to get info on
 */
case class InfoUsersRequest(include_locale: Option[Boolean], user: Option[String])

object InfoUsersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoUsersRequest] = FormEncoder.fromParams.contramap[InfoUsersRequest] { req =>
    List("include_locale" -> req.include_locale, "user" -> req.user)
  }
}

/**
 * @param limit The maximum number of items to return. Fewer than the requested number of items may be returned, even if the end of the users list hasn't been reached. Providing no `limit` value will result in Slack attempting to deliver you the entire result set. If the collection is too large you may experience `limit_required` or HTTP 500 errors.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 * @param include_locale Set this to `true` to receive the locale for users. Defaults to `false`
 */
case class ListUsersRequest(limit: Option[Int], cursor: Option[String], include_locale: Option[Boolean])

object ListUsersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListUsersRequest] = FormEncoder.fromParams.contramap[ListUsersRequest] { req =>
    List("limit" -> req.limit, "cursor" -> req.cursor, "include_locale" -> req.include_locale)
  }
}

/**
 * @param email An email address belonging to a user in the workspace
 */
case class LookupByEmailUsersRequest(email: String)

object LookupByEmailUsersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[LookupByEmailUsersRequest] =
    FormEncoder.fromParams.contramap[LookupByEmailUsersRequest] { req =>
      List("email" -> req.email)
    }
}

/**
 * @param presence Either `auto` or `away`
 */
case class SetPresenceUsersRequest(presence: String)

object SetPresenceUsersRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[SetPresenceUsersRequest] = deriveEncoder[SetPresenceUsersRequest]
}
