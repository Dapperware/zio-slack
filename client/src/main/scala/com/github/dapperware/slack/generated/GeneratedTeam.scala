/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedTeam {

  /**
   * Gets the access logs for the current team.
   * @see https://api.slack.com/methods/team.accessLogs
   */
  def accessLogsTeam(req: AccessLogsTeamRequest): Request[AccessLogsTeamResponse, AccessToken] =
    request("team.accessLogs").formBody(req).as[AccessLogsTeamResponse].auth.accessToken

  /**
   * Gets billable users information for the current team.
   * @see https://api.slack.com/methods/team.billableInfo
   */
  def billableInfoTeam(req: BillableInfoTeamRequest): Request[Unit, AccessToken] =
    request("team.billableInfo").formBody(req).auth.accessToken

  /**
   * Gets information about the current team.
   * @see https://api.slack.com/methods/team.info
   */
  def infoTeam(req: InfoTeamRequest): Request[InfoTeamResponse, AccessToken] =
    request("team.info").formBody(req).as[InfoTeamResponse].auth.accessToken

  /**
   * Gets the integration logs for the current team.
   * @see https://api.slack.com/methods/team.integrationLogs
   */
  def integrationLogsTeam(req: IntegrationLogsTeamRequest): Request[IntegrationLogsTeamResponse, AccessToken] =
    request("team.integrationLogs").formBody(req).as[IntegrationLogsTeamResponse].auth.accessToken

}
