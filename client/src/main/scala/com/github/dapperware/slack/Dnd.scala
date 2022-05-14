package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedRequest
import com.github.dapperware.slack.api.{DndInfo, SnoozeInfo, as, request, sendM}
import zio.{Has, URIO, ZIO}

trait Dnd {
  def endDnd(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] = {
    Request.make("dnd.endDnd").toCall
  }

  def endSnooze(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request.make("dnd.endSnooze").toCall


  def getDoNotDisturbInfo(userId: Option[String] = None): ZIO[SlackEnv, SlackError, DndInfo] = {
    Request.make("dnd.info")
    sendM(request("dnd.info")) >>= as[DndInfo]
  }

  def setSnooze(numMinutes: Int): ZIO[SlackEnv, SlackError, SnoozeInfo] =
    sendM(request("dnd.setSnooze", "num_minutes" -> numMinutes)) >>= as[SnoozeInfo]

  def getTeamDoNotDisturbInfo(users: List[String]): ZIO[SlackEnv, SlackError, Map[String, DndInfo]] =
    sendM(request("dnd.teamInfo", "users" -> users.mkString(","))) >>= as[Map[String, DndInfo]]("users")
}
