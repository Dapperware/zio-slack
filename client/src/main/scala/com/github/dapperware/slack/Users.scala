package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.generated.GeneratedUsers
import com.github.dapperware.slack.generated.requests.{
  GetPresenceUsersRequest,
  ListUsersRequest,
  LookupByEmailUsersRequest,
  SetPresenceUsersRequest
}
import com.github.dapperware.slack.generated.responses.{
  GetPresenceUsersResponse,
  InfoUsersResponse,
  ListUsersResponse
}
import com.github.dapperware.slack.models.User
import zio.{ Has, URIO }

trait Users {
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[GetPresenceUsersResponse]] =
    Users.getPresenceUsers(GetPresenceUsersRequest(Some(userId))).toCall

  def getUserInfo(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    Users.infoUsers(InfoUsersResponse(user = userId)).toCall

  def listUsers(
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    includeLocale: Option[Boolean] = None,
    presence: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListUsersResponse]] =
    Users
      .listUsers(
        ListUsersRequest(
          limit = limit,
          cursor = cursor,
          include_locale = includeLocale
        )
      )
      .toCall

  def setUserActive(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Users.setActiveUsers.toCall

  def setUserPresence(presence: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Users.setPresenceUsers(SetPresenceUsersRequest(presence)).toCall

  def lookupUserByEmail(emailId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    Users.lookupByEmailUsers(LookupByEmailUsersRequest(email = emailId)).map(_.user).toCall

  def deletePhoto: URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Users.deletePhotoUsers.toCall

  def setPhoto(
    entity: RequestEntity,
    cropW: Option[Int] = None,
    cropX: Option[Int] = None,
    cropY: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("users.setPhoto")
      .entityBody(entity)("crop_w" -> cropW, "crop_x" -> cropX, "crop_y" -> cropY)
      .toCall

}

object Users extends GeneratedUsers
