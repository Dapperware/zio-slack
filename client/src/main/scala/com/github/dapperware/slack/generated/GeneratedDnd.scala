/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedDnd {

  /**
   * Ends the current user's Do Not Disturb session immediately.
   * @see https://api.slack.com/methods/dnd.endDnd
   */
  def endDndDnd: Request[Unit, AccessToken] =
    request("dnd.endDnd").auth.accessToken

  /**
   * Ends the current user's snooze mode immediately.
   * @see https://api.slack.com/methods/dnd.endSnooze
   */
  def endSnoozeDnd: Request[EndSnoozeDndResponse, AccessToken] =
    request("dnd.endSnooze").as[EndSnoozeDndResponse].auth.accessToken

  /**
   * Retrieves a user's current Do Not Disturb status.
   * @see https://api.slack.com/methods/dnd.info
   */
  def infoDnd(req: InfoDndRequest): Request[InfoDndResponse, AccessToken] =
    request("dnd.info").formBody(req).as[InfoDndResponse].auth.accessToken

  /**
   * Turns on Do Not Disturb mode for the current user, or changes its duration.
   * @see https://api.slack.com/methods/dnd.setSnooze
   */
  def setSnoozeDnd(req: SetSnoozeDndRequest): Request[SetSnoozeDndResponse, AccessToken] =
    request("dnd.setSnooze").formBody(req).as[SetSnoozeDndResponse].auth.accessToken

  /**
   * Retrieves the Do Not Disturb status for up to 50 users on a team.
   * @see https://api.slack.com/methods/dnd.teamInfo
   */
  def teamInfoDnd(req: TeamInfoDndRequest): Request[Unit, AccessToken] =
    request("dnd.teamInfo").formBody(req).auth.accessToken

}
