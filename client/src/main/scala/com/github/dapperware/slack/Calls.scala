package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedCalls
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.models.Call
import io.circe.syntax._
import io.circe.{ Encoder, Json }
import zio.duration.Duration
import zio.{ Has, URIO }

import java.time.Instant

trait Calls { self: Slack =>

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
  ): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(
      Calls
        .addCalls(
          AddCallsRequest(
            external_unique_id = externalUniqueId,
            join_url = joinUrl,
            external_display_id = externalDisplayId,
            desktop_app_join_url = desktopAppJoinUrl,
            date_start = dateStart.map(_.getEpochSecond.toInt),
            title = title,
            created_by = createdBy,
            users = users.map(_.asJson.noSpaces)
          )
        )
        .map(_.call)
    )

  /**
   * https://api.slack.com/methods/calls.end
   */
  def endCall(
    id: String,
    duration: Option[Duration]
  ): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(Calls.endCalls(EndCallsRequest(id, duration = duration.map(_.toSeconds.toInt))).map(_.call))

  /**
   * https://api.slack.com/methods/calls.info
   */
  def getCallInfo(id: String): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(Calls.infoCalls(InfoCallsRequest(id)).map(_.call))

  /**
   * https://api.slack.com/methods/calls.update
   */
  def updateCall(
    id: String,
    desktopAppJoinUrl: Option[String],
    joinUrl: Option[String],
    title: Option[String]
  ): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(
      Calls
        .updateCalls(
          UpdateCallsRequest(id = id, title = title, join_url = joinUrl, desktop_app_join_url = desktopAppJoinUrl)
        )
        .map(_.call)
    )

  def addParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(
      Calls
        .addParticipantsCalls(AddParticipantsCallsRequest(id = id, users = users.asJson.noSpaces))
        .map(_.call)
    )

  def removeParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[AccessToken], SlackResponse[Call]] =
    apiCall(
      Calls
        .removeParticipantsCalls(RemoveParticipantsCallsRequest(id = id, users = users.asJson.noSpaces))
        .map(_.call)
    )
}

private[slack] trait CallsAccessors {
  _: Slack.type =>

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
    URIO.accessM(
      _.get.addCall(externalUniqueId, joinUrl, createdBy, dateStart, desktopAppJoinUrl, externalDisplayId, title, users)
    )

  /**
   * https://api.slack.com/methods/calls.end
   */
  def endCall(
    id: String,
    duration: Option[Duration]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    URIO.accessM(_.get.endCall(id, duration))

  /**
   * https://api.slack.com/methods/calls.info
   */
  def getCallInfo(id: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    URIO.accessM(_.get.getCallInfo(id))

  /**
   * https://api.slack.com/methods/calls.update
   */
  def updateCall(
    id: String,
    desktopAppJoinUrl: Option[String],
    joinUrl: Option[String],
    title: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    URIO.accessM(_.get.updateCall(id, desktopAppJoinUrl, joinUrl, title))

  def addParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    URIO.accessM(_.get.addParticipants(id, users))

  def removeParticipants(
    id: String,
    users: List[CallParticipant]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Call]] =
    URIO.accessM(_.get.removeParticipants(id, users))
}

object Calls extends GeneratedCalls

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
