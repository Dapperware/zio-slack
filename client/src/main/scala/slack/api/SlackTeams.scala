package slack.api

import io.circe.Json
import slack.{SlackEnv, SlackError}
import zio.ZIO
import zio.macros.annotation.mockable

@mockable
//@accessible
trait SlackTeams {
  val slackTeams: SlackTeams.Service[Any]
}

object SlackTeams {
  trait Service[R] {
    // TODO: Parse actual result type: https://api.slack.com/methods/team.accessLogs
    def getTeamAccessLogs(count: Option[Int], page: Option[Int]): ZIO[R with SlackEnv, SlackError, Json] =
      sendM(request("team.accessLogs", "count" -> count, "page" -> page))

    // TODO: Parse actual value type: https://api.slack.com/methods/team.info
    def getTeamInfo: ZIO[R with SlackEnv, SlackError, Json] =
      sendM(request("team.info"))
  }
}

object teams extends SlackTeams.Service[SlackEnv]
