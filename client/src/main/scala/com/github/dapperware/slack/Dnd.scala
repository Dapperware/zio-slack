package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedDnd
import com.github.dapperware.slack.generated.requests.{ SetSnoozeDndRequest, TeamInfoDndRequest }
import com.github.dapperware.slack.generated.responses.{ EndSnoozeDndResponse, SetSnoozeDndResponse }
import com.github.dapperware.slack.models.DndInfo
import zio.{ Has, URIO }

trait Dnd {
  def endDnd(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Dnd.endDndDnd.toCall

  def endSnooze(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[EndSnoozeDndResponse]] =
    Dnd.endSnoozeDnd.toCall

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

  def setSnooze(numMinutes: Int): URIO[Has[Slack] with Has[AccessToken], SlackResponse[SetSnoozeDndResponse]] =
    Dnd.setSnoozeDnd(SetSnoozeDndRequest(numMinutes.toString)).toCall

  // FIXME This is the wrong return type
  def getTeamDoNotDisturbInfo(users: List[String]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Dnd.teamInfoDnd(TeamInfoDndRequest(Some(users).filter(_.nonEmpty).map(_.mkString(",")))).toCall
}

object Dnd extends GeneratedDnd
