package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedRequest
import com.github.dapperware.slack.models.{Call, CallParticipant}
import io.circe.Json
import io.circe.syntax._
import zio.{Has, URIO}
import zio.duration.Duration

import java.time.Instant

trait Calls {

  def addCall(
    externalUniqueId: String,
    joinUrl: String,
    createdBy: Option[String] = None,
    dateStart: Option[Instant] = None,
    desktopAppJoinUrl: Option[String] = None,
    externalDisplayId: Option[String] = None,
    title: Option[String] = None,
    users: Option[List[CallParticipant]]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    Request
      .make("calls.add")
      .jsonBody(
        Json.obj(
          "external_unique_id"   -> externalUniqueId.asJson,
          "join_url"             -> joinUrl.asJson,
          "created_by"           -> createdBy.asJson,
          "date_start"           -> dateStart.asJson,
          "desktop_app_join_url" -> desktopAppJoinUrl.asJson,
          "external_display_id"  -> externalDisplayId.asJson,
          "title"                -> title.asJson,
          "users"                -> users.asJson
        )
      ).at[Call]("call").toCall

  def endCall(id: String, duration: Option[Duration]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    Request.make("calls.end").jsonBody(
      Json.obj(
        "id" -> id.asJson,
        "duration" -> duration.map(_.toSeconds).asJson
      )
    ).at[Call]("call").toCall

  def getCallInfo(id: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    Request.make("calls.info").jsonBody(Json.obj("id" -> id.asJson)).at[Call]("call").toCall

  def updateCall(
                  id: String,
                  desktopAppJoinUrl: Option[String],
                  joinUrl: Option[String],
                  title: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    Request
      .make("calls.update")
      .jsonBody(
        Json.obj(
          "id"                   -> id.asJson,
          "desktop_app_join_url" -> desktopAppJoinUrl.asJson,
          "join_url"             -> joinUrl.asJson,
          "title"                -> title.asJson
        )
      ).at[Call]("call").toCall
}
