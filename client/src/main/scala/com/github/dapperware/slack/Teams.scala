package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedTeam
import com.github.dapperware.slack.generated.requests.{ AccessLogsTeamRequest, InfoTeamRequest }
import com.github.dapperware.slack.generated.responses.{ AccessLogsTeamResponse, InfoTeamResponse }
import zio.{ Has, URIO }

trait Teams { self: Slack =>

  def getTeamAccessLogs(
    count: Option[Int] = None,
    page: Option[Int] = None,
    before: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[AccessLogsTeamResponse]] =
    apiCall(Teams.accessLogsTeam(AccessLogsTeamRequest(count = count, page = page, before = before)))

  def getTeamInfo: URIO[Has[AccessToken], SlackResponse[InfoTeamResponse]] =
    apiCall(Teams.infoTeam(InfoTeamRequest(None)))
}

private[slack] trait TeamsAccessors { _: Slack.type =>

  def getTeamAccessLogs(
    count: Option[Int] = None,
    page: Option[Int] = None,
    before: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[AccessLogsTeamResponse]] =
    URIO.accessM(_.get.getTeamAccessLogs(count, page, before))

  def getTeamInfo: URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoTeamResponse]] =
    URIO.accessM(_.get.getTeamInfo)
}

object Teams extends GeneratedTeam
