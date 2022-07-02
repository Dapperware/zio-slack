/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class CreateUsergroupsResponse(usergroup: com.github.dapperware.slack.models.UserGroup)

object CreateUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[CreateUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[CreateUsergroupsResponse]
}

case class DisableUsergroupsResponse(usergroup: com.github.dapperware.slack.models.UserGroup)

object DisableUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[DisableUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[DisableUsergroupsResponse]
}

case class EnableUsergroupsResponse(usergroup: com.github.dapperware.slack.models.UserGroup)

object EnableUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[EnableUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[EnableUsergroupsResponse]
}

case class ListUsergroupsResponse(usergroups: zio.Chunk[com.github.dapperware.slack.models.UserGroup])

object ListUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[ListUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListUsergroupsResponse]
}

case class UpdateUsergroupsResponse(usergroup: com.github.dapperware.slack.models.UserGroup)

object UpdateUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[UpdateUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateUsergroupsResponse]
}

case class ListUsersUsergroupsResponse(users: List[String])

object ListUsersUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[ListUsersUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListUsersUsergroupsResponse]
}

case class UpdateUsersUsergroupsResponse(usergroup: com.github.dapperware.slack.models.UserGroup)

object UpdateUsersUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[UpdateUsersUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[UpdateUsersUsergroupsResponse]
}
