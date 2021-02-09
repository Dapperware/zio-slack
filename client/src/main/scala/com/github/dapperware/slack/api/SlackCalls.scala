package com.github.dapperware.slack.api

import com.github.dapperware.slack.SlackEnv
import com.github.dapperware.slack.models.{ Call, CallParticipant }
import io.circe.Json
import io.circe.syntax._
import zio.ZIO
import zio.duration.Duration

import java.time.Instant

trait SlackCalls {

  def addCall(
    externalUniqueId: String,
    joinUrl: String,
    createdBy: Option[String] = None,
    dateStart: Option[Instant] = None,
    desktopAppJoinUrl: Option[String] = None,
    externalDisplayId: Option[String] = None,
    title: Option[String] = None,
    users: Option[List[CallParticipant]]
  ) =
    sendM(
      requestJson(
        "calls.add",
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
      )
    ) >>= as[Call]("call")

  def endCall(id: String, duration: Option[Duration]): ZIO[SlackEnv, Throwable, Boolean] =
    sendM(
      requestJson("calls.end", Json.obj("id" -> id.asJson, "duration" -> duration.map(_.toSeconds).asJson))
    ) >>= isOk

  def getCallInfo(id: String): ZIO[SlackEnv, Throwable, Call] =
    sendM(requestJson("calls.info", Json.obj("id" -> id.asJson))) >>= as[Call]("call")

  def updateCall(id: String,
                 desktopAppJoinUrl: Option[String],
                 joinUrl: Option[String],
                 title: Option[String]): ZIO[SlackEnv, Throwable, Call] =
    sendM(
      requestJson(
        "calls.update",
        Json.obj(
          "id"                   -> id.asJson,
          "desktop_app_join_url" -> desktopAppJoinUrl.asJson,
          "join_url"             -> joinUrl.asJson,
          "title"                -> title.asJson
        )
      )
    ) >>= as[Call]("call")
}

object calls extends SlackCalls
