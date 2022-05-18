/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class AccessLogsTeamResponse(logins: List[String], paging: com.github.dapperware.slack.models.PagingObject)

object AccessLogsTeamResponse {
  implicit val decoder: io.circe.Decoder[AccessLogsTeamResponse] =
    io.circe.generic.semiauto.deriveDecoder[AccessLogsTeamResponse]
}

case class InfoTeamResponse(team: com.github.dapperware.slack.models.Team)

object InfoTeamResponse {
  implicit val decoder: io.circe.Decoder[InfoTeamResponse] = io.circe.generic.semiauto.deriveDecoder[InfoTeamResponse]
}

case class IntegrationLogsTeamResponse(logs: List[String], paging: com.github.dapperware.slack.models.PagingObject)

object IntegrationLogsTeamResponse {
  implicit val decoder: io.circe.Decoder[IntegrationLogsTeamResponse] =
    io.circe.generic.semiauto.deriveDecoder[IntegrationLogsTeamResponse]
}
