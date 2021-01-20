package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import io.circe.Json
import zio.ZIO

trait SlackTeams {
  // TODO: Parse actual result type: https://api.slack.com/methods/team.accessLogs
  def getTeamAccessLogs(count: Option[Int], page: Option[Int]): ZIO[SlackEnv, SlackError, Json] =
    sendM(request("team.accessLogs", "count" -> count, "page" -> page))

  // TODO: Parse actual value type: https://api.slack.com/methods/team.info
  def getTeamInfo: ZIO[SlackEnv, SlackError, Json] =
    sendM(request("team.info"))
}

object teams extends SlackTeams
