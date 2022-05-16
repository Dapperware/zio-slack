package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.api.{ DndInfo, SnoozeInfo }
import zio.{ Has, URIO, ZIO }

trait Dnd {
  def endDnd(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("dnd.endDnd").toCall

  def endSnooze(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("dnd.endSnooze").toCall

  def getDoNotDisturbInfo(
    userId: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[DndInfo]] =
    request("dnd.info")
      .formBody(
        "user_id" -> userId,
        "team_id" -> teamId
      )
      .as[DndInfo]
      .toCall

  def setSnooze(numMinutes: Int): Request[SnoozeInfo, AccessToken]                             =
    request("dnd.setSnooze").formBody(Map("num_minutes" -> numMinutes.toString)).as[SnoozeInfo]

  def getTeamDoNotDisturbInfo(users: List[String]): Request[Map[String, DndInfo], AccessToken] =
    request("dnd.teamInfo").formBody(Map("users" -> users.mkString(","))).at[Map[String, DndInfo]]("users")
}
