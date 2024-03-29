/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param before End of time range of logs to include in results (inclusive).
 * @param count undefined
 * @param page undefined
 */
case class AccessLogsTeamRequest(before: Option[String], count: Option[Int], page: Option[Int])

object AccessLogsTeamRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AccessLogsTeamRequest] = FormEncoder.fromParams.contramap[AccessLogsTeamRequest] {
    req =>
      List("before" -> req.before, "count" -> req.count, "page" -> req.page)
  }
}

/**
 * @param user A user to retrieve the billable information for. Defaults to all users.
 */
case class BillableInfoTeamRequest(user: Option[String])

object BillableInfoTeamRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[BillableInfoTeamRequest] =
    FormEncoder.fromParams.contramap[BillableInfoTeamRequest] { req =>
      List("user" -> req.user)
    }
}

/**
 * @param team Team to get info on, if omitted, will return information about the current team. Will only return team that the authenticated token is allowed to see through external shared channels
 */
case class InfoTeamRequest(team: Option[String])

object InfoTeamRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoTeamRequest] = FormEncoder.fromParams.contramap[InfoTeamRequest] { req =>
    List("team" -> req.team)
  }
}

/**
 * @param app_id Filter logs to this Slack app. Defaults to all logs.
 * @param change_type Filter logs with this change type. Defaults to all logs.
 * @param count undefined
 * @param page undefined
 * @param service_id Filter logs to this service. Defaults to all logs.
 * @param user Filter logs generated by this user’s actions. Defaults to all logs.
 */
case class IntegrationLogsTeamRequest(
  app_id: Option[String],
  change_type: Option[String],
  count: Option[String],
  page: Option[String],
  service_id: Option[String],
  user: Option[String]
)

object IntegrationLogsTeamRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[IntegrationLogsTeamRequest] =
    FormEncoder.fromParams.contramap[IntegrationLogsTeamRequest] { req =>
      List(
        "app_id"      -> req.app_id,
        "change_type" -> req.change_type,
        "count"       -> req.count,
        "page"        -> req.page,
        "service_id"  -> req.service_id,
        "user"        -> req.user
      )
    }
}
