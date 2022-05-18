/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class ListUsergroupsResponse(usergroups: List[String])

object ListUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[ListUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListUsergroupsResponse]
}

case class ListUsersUsergroupsResponse(users: List[String])

object ListUsersUsergroupsResponse {
  implicit val decoder: io.circe.Decoder[ListUsersUsergroupsResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListUsersUsergroupsResponse]
}
