package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.models.{ Call, CallParticipant }
import io.circe.{ Encoder, Json }
import io.circe.syntax._
import zio.{ Has, URIO }
import zio.duration.Duration

import java.time.Instant

trait Calls {

  /**
   * https://api.slack.com/methods/calls.add
   */
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
    request("calls.add")
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
      )
      .at[Call]("call")
      .toCall

  /**
   * https://api.slack.com/methods/calls.end
   */
  def endCall(id: String, duration: Option[Duration]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    request("calls.end")
      .jsonBody(
        Json.obj(
          "id"       -> id.asJson,
          "duration" -> duration.map(_.toSeconds).asJson
        )
      )
      .at[Call]("call")
      .toCall

  /**
   * https://api.slack.com/methods/calls.info
   */
  def getCallInfo(id: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    request("calls.info").jsonBody(Json.obj("id" -> id.asJson)).at[Call]("call").toCall

  /**
   * https://api.slack.com/methods/calls.update
   */
  def updateCall(
    id: String,
    desktopAppJoinUrl: Option[String],
    joinUrl: Option[String],
    title: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]]                           =
    request("calls.update")
      .jsonBody(
        Json.obj(
          "id"                   -> id.asJson,
          "desktop_app_join_url" -> desktopAppJoinUrl.asJson,
          "join_url"             -> joinUrl.asJson,
          "title"                -> title.asJson
        )
      )
      .at[Call]("call")
      .toCall

  def addParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("calls.participants.add")
      .jsonBody(
        Json.obj(
          "id"    -> id.asJson,
          "users" -> users.asJson
        )
      )
      .toCall

  def removeParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("calls.participants.remove")
      .jsonBody(
        Json.obj(
          "id"    -> id.asJson,
          "users" -> users.asJson
        )
      )
      .toCall
}

sealed trait CallParticipant

object CallParticipant {
  case class User(id: String)                                                                 extends CallParticipant
  case class ExternalUser(externalId: String, displayName: String, avatarUrl: Option[String]) extends CallParticipant

  implicit val encoder: Encoder[CallParticipant] = Encoder.instance {
    case User(id)                                            => Json.obj("id" -> id.asJson)
    case ExternalUser(external_id, display_name, avatar_url) =>
      Json.obj(
        "external_id"  -> external_id.asJson,
        "display_name" -> display_name.asJson,
        "avatar_url"   -> avatar_url.asJson
      )
  }
}
