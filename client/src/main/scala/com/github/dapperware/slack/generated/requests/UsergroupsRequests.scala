/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param name A name for the User Group. Must be unique among User Groups.
 * @param channels A comma separated string of encoded channel IDs for which the User Group uses as a default.
 * @param description A short description of the User Group.
 * @param handle A mention handle. Must be unique among channels, users and User Groups.
 * @param include_count Include the number of users in each User Group.
 */
case class CreateUsergroupsRequest(
  name: String,
  channels: Option[String],
  description: Option[String],
  handle: Option[String],
  include_count: Option[Boolean]
)

object CreateUsergroupsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[CreateUsergroupsRequest] = deriveEncoder[CreateUsergroupsRequest]
}

/**
 * @param usergroup The encoded ID of the User Group to disable.
 * @param include_count Include the number of users in the User Group.
 */
case class DisableUsergroupsRequest(usergroup: String, include_count: Option[Boolean])

object DisableUsergroupsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[DisableUsergroupsRequest] = deriveEncoder[DisableUsergroupsRequest]
}

/**
 * @param usergroup The encoded ID of the User Group to enable.
 * @param include_count Include the number of users in the User Group.
 */
case class EnableUsergroupsRequest(usergroup: String, include_count: Option[Boolean])

object EnableUsergroupsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[EnableUsergroupsRequest] = deriveEncoder[EnableUsergroupsRequest]
}

/**
 * @param include_users Include the list of users for each User Group.
 * @param include_count Include the number of users in each User Group.
 * @param include_disabled Include disabled User Groups.
 */
case class ListUsergroupsRequest(
  include_users: Option[Boolean],
  include_count: Option[Boolean],
  include_disabled: Option[Boolean]
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
 * @param handle A mention handle. Must be unique among channels, users and User Groups.
 * @param description A short description of the User Group.
 * @param channels A comma separated string of encoded channel IDs for which the User Group uses as a default.
 * @param include_count Include the number of users in the User Group.
 * @param name A name for the User Group. Must be unique among User Groups.
 */
case class UpdateUsergroupsRequest(
  usergroup: String,
  handle: Option[String],
  description: Option[String],
  channels: Option[String],
  include_count: Option[Boolean],
  name: Option[String]
)

object UpdateUsergroupsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[UpdateUsergroupsRequest] = deriveEncoder[UpdateUsergroupsRequest]
}

/**
 * @param usergroup The encoded ID of the User Group to update.
 * @param include_disabled Allow results that involve disabled User Groups.
 */
case class ListUsersUsergroupsRequest(usergroup: String, include_disabled: Option[Boolean])

object ListUsersUsergroupsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListUsersUsergroupsRequest] =
    FormEncoder.fromParams.contramap[ListUsersUsergroupsRequest] { req =>
      List("usergroup" -> req.usergroup, "include_disabled" -> req.include_disabled)
    }
}

/**
 * @param usergroup The encoded ID of the User Group to update.
 * @param users A comma separated string of encoded user IDs that represent the entire list of users for the User Group.
 * @param include_count Include the number of users in the User Group.
 */
case class UpdateUsersUsergroupsRequest(usergroup: String, users: String, include_count: Option[Boolean])

object UpdateUsersUsergroupsRequest {
  import io.circe.Encoder
  import io.circe.generic.semiauto.deriveEncoder
  implicit val encoder: Encoder[UpdateUsersUsergroupsRequest] = deriveEncoder[UpdateUsersUsergroupsRequest]
}
