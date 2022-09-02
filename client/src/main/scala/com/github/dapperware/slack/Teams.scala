package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedTeam
import com.github.dapperware.slack.generated.requests.{ AccessLogsTeamRequest, InfoTeamRequest }
import com.github.dapperware.slack.generated.responses.{ AccessLogsTeamResponse, InfoTeamResponse }
import zio.{ Trace, URIO, ZIO }

trait Teams { self: SlackApiBase =>

  def getTeamAccessLogs(
    count: Option[Int] = None,
    page: Option[Int] = None,
    before: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[AccessLogsTeamResponse]] =
    apiCall(Teams.accessLogsTeam(AccessLogsTeamRequest(count = count, page = page, before = before)))

  def getTeamInfo: URIO[AccessToken, SlackResponse[InfoTeamResponse]] =
    apiCall(Teams.infoTeam(InfoTeamRequest(None)))
}

private[slack] trait TeamsAccessors { self: Slack.type =>

  def getTeamAccessLogs(
    count: Option[Int] = None,
    page: Option[Int] = None,
    before: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[AccessLogsTeamResponse]] =
    ZIO.serviceWithZIO[Slack](_.getTeamAccessLogs(count, page, before))

  def getTeamInfo: URIO[Slack with AccessToken, SlackResponse[InfoTeamResponse]] =
    ZIO.serviceWithZIO[Slack](_.getTeamInfo)
}

object Teams extends GeneratedTeam
