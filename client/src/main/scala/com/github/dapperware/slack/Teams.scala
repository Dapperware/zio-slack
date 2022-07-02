package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedTeam
import com.github.dapperware.slack.generated.requests.{ AccessLogsTeamRequest, InfoTeamRequest }
import com.github.dapperware.slack.generated.responses.{ AccessLogsTeamResponse, InfoTeamResponse }
import zio.{ Has, URIO }

trait Teams {

  def getTeamAccessLogs(
    count: Option[Int] = None,
    page: Option[Int] = None,
    before: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[AccessLogsTeamResponse]] =
    Teams.accessLogsTeam(AccessLogsTeamRequest(count = count, page = page, before = before)).toCall

  def getTeamInfo: URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoTeamResponse]] =
    Teams.infoTeam(InfoTeamRequest(None)).toCall
}

object Teams extends GeneratedTeam
