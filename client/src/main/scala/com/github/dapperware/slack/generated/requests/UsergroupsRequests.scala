/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param include_users Include the list of users for each User Group.
 * @param include_count Include the number of users in each User Group.
 * @param include_disabled Include disabled User Groups.
 */
case class ListUsergroupsRequest(
  include_users: Option[Boolean] = None,
  include_count: Option[Boolean] = None,
  include_disabled: Option[Boolean] = None
)

object ListUsergroupsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListUsergroupsRequest] = FormEncoder.fromParams.contramap[ListUsergroupsRequest] {
    req =>
      List(
        "include_users"    -> req.include_users,
        "include_count"    -> req.include_count,
        "include_disabled" -> req.include_disabled
      )
  }
}

/**
 * @param usergroup The encoded ID of the User Group to update.
 * @param include_disabled Allow results that involve disabled User Groups.
 */
case class ListUsersUsergroupsRequest(usergroup: String, include_disabled: Option[Boolean] = None)

object ListUsersUsergroupsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListUsersUsergroupsRequest] =
    FormEncoder.fromParams.contramap[ListUsersUsergroupsRequest] { req =>
      List("usergroup" -> req.usergroup, "include_disabled" -> req.include_disabled)
    }
}
