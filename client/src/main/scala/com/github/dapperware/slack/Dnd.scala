package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedDnd
import com.github.dapperware.slack.generated.requests.{ SetSnoozeDndRequest, TeamInfoDndRequest }
import com.github.dapperware.slack.generated.responses.{ EndSnoozeDndResponse, SetSnoozeDndResponse }
import com.github.dapperware.slack.models.DndInfo
import zio.{ URIO, ZIO }

trait Dnd { self: Slack =>
  def endDnd(): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Dnd.endDndDnd)

  def endSnooze(): URIO[AccessToken, SlackResponse[EndSnoozeDndResponse]] =
    apiCall(Dnd.endSnoozeDnd)

  def getDoNotDisturbInfo(
    userId: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[AccessToken, SlackResponse[DndInfo]] =
    apiCall(
      request("dnd.info")
        .formBody(
          "user_id" -> userId,
          "team_id" -> teamId
        )
        .as[DndInfo]
    )

  def setSnooze(numMinutes: Int): URIO[AccessToken, SlackResponse[SetSnoozeDndResponse]] =
    apiCall(Dnd.setSnoozeDnd(SetSnoozeDndRequest(numMinutes.toString)))

  // FIXME This is the wrong return type
  def getTeamDoNotDisturbInfo(users: List[String]): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Dnd.teamInfoDnd(TeamInfoDndRequest(Some(users).filter(_.nonEmpty).map(_.mkString(",")))))
}

private[slack] trait DndAccessors { _: Slack.type =>
  def endDnd(): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.endDnd())

  def endSnooze(): URIO[Slack with AccessToken, SlackResponse[EndSnoozeDndResponse]] =
    ZIO.serviceWithZIO[Slack](_.endSnooze())

  def getDoNotDisturbInfo(
    userId: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[Slack with AccessToken, SlackResponse[DndInfo]] =
    ZIO.serviceWithZIO[Slack](_.getDoNotDisturbInfo(userId, teamId))

  def setSnooze(numMinutes: Int): URIO[Slack with AccessToken, SlackResponse[SetSnoozeDndResponse]] =
    ZIO.serviceWithZIO[Slack](_.setSnooze(numMinutes))

  // FIXME This is the wrong return type
  def getTeamDoNotDisturbInfo(users: List[String]): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.getTeamDoNotDisturbInfo(users))
}

object Dnd extends GeneratedDnd
