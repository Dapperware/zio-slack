/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param user User to fetch status for (defaults to current user)
 */
case class InfoDndRequest(user: Option[String] = None)

object InfoDndRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoDndRequest] = FormEncoder.fromParams.contramap[InfoDndRequest] { req =>
    List("user" -> req.user)
  }
}

/**
 * @param num_minutes Number of minutes, from now, to snooze until.
 */
case class SetSnoozeDndRequest(num_minutes: String)

object SetSnoozeDndRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[SetSnoozeDndRequest] = FormEncoder.fromParams.contramap[SetSnoozeDndRequest] {
    req =>
      List("num_minutes" -> req.num_minutes)
  }
}

/**
 * @param users Comma-separated list of users to fetch Do Not Disturb status for
 */
case class TeamInfoDndRequest(users: Option[String] = None)

object TeamInfoDndRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[TeamInfoDndRequest] = FormEncoder.fromParams.contramap[TeamInfoDndRequest] { req =>
    List("users" -> req.users)
  }
}
