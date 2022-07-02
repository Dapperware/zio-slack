package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedDnd
import com.github.dapperware.slack.generated.requests.{ SetSnoozeDndRequest, TeamInfoDndRequest }
import com.github.dapperware.slack.generated.responses.{ EndSnoozeDndResponse, SetSnoozeDndResponse }
import com.github.dapperware.slack.models.DndInfo
import zio.{ Has, URIO }

trait Dnd { self: Slack =>
  def endDnd(): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Dnd.endDndDnd)

  def endSnooze(): URIO[Has[AccessToken], SlackResponse[EndSnoozeDndResponse]] =
    apiCall(Dnd.endSnoozeDnd)

  def getDoNotDisturbInfo(
    userId: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[DndInfo]] =
    apiCall(
      request("dnd.info")
        .formBody(
          "user_id" -> userId,
          "team_id" -> teamId
        )
        .as[DndInfo]
    )

  def setSnooze(numMinutes: Int): URIO[Has[AccessToken], SlackResponse[SetSnoozeDndResponse]] =
    apiCall(Dnd.setSnoozeDnd(SetSnoozeDndRequest(numMinutes.toString)))

  // FIXME This is the wrong return type
  def getTeamDoNotDisturbInfo(users: List[String]): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Dnd.teamInfoDnd(TeamInfoDndRequest(Some(users).filter(_.nonEmpty).map(_.mkString(",")))))
}

private[slack] trait DndAccessors { _: Slack.type =>
  def endDnd(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.endDnd())

  def endSnooze(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[EndSnoozeDndResponse]] =
    URIO.accessM(_.get.endSnooze())

  def getDoNotDisturbInfo(
    userId: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[DndInfo]] =
    URIO.accessM(_.get.getDoNotDisturbInfo(userId, teamId))

  def setSnooze(numMinutes: Int): URIO[Has[Slack] with Has[AccessToken], SlackResponse[SetSnoozeDndResponse]] =
    URIO.accessM(_.get.setSnooze(numMinutes))

  // FIXME This is the wrong return type
  def getTeamDoNotDisturbInfo(users: List[String]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.getTeamDoNotDisturbInfo(users))
}

object Dnd extends GeneratedDnd
