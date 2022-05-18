package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.generated.GeneratedUsers
import com.github.dapperware.slack.generated.requests.{
  GetPresenceUsersRequest,
  ListUsersRequest,
  SetPresenceUsersRequest
}
import com.github.dapperware.slack.generated.responses.{
  GetPresenceUsersResponse,
  InfoUsersResponse,
  ListUsersResponse
}
import com.github.dapperware.slack.models.User
import zio.{ Has, URIO, ZIO }

trait Users {
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[GetPresenceUsersResponse]] =
    Users.getPresenceUsers(GetPresenceUsersRequest(Some(userId))).toCall

  def getUserInfo(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    Users.infoUsers(InfoUsersResponse()).toCall

  def listUsers(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListUsersResponse]] =
    Users.listUsers(ListUsersRequest()).toCall

  def setUserActive(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Users.setActiveUsers.toCall

  def setUserPresence(presence: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Users.setPresenceUsers(SetPresenceUsersRequest(presence)).toCall

  def lookupUserByEmail(emailId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    request("users.lookupByEmail")
      .formBody(Map("email" -> emailId))
      .at[User]("user")
      .toCall

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
