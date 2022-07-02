package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.generated.GeneratedUsers
import com.github.dapperware.slack.generated.requests.{
  GetPresenceUsersRequest,
  InfoUsersRequest,
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

trait Users { self: Slack =>
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[AccessToken], SlackResponse[GetPresenceUsersResponse]] =
    apiCall(Users.getPresenceUsers(GetPresenceUsersRequest(Some(userId))))

  def getUserInfo(
    userId: String,
    includeLocale: Option[Boolean] = None
  ): URIO[Has[AccessToken], SlackResponse[InfoUsersResponse]] =
    apiCall(Users.infoUsers(InfoUsersRequest(user = Some(userId), include_locale = includeLocale)))

  def listUsers(
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    includeLocale: Option[Boolean] = None,
    presence: Option[Boolean] = None
  ): URIO[Has[AccessToken], SlackResponse[ListUsersResponse]] =
    apiCall(
      Users
        .listUsers(
          ListUsersRequest(
            limit = limit,
            cursor = cursor,
            include_locale = includeLocale
          )
        )
    )

  def setUserActive(): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Users.setActiveUsers)

  def setUserPresence(presence: String): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Users.setPresenceUsers(SetPresenceUsersRequest(presence)))

  def lookupUserByEmail(emailId: String): URIO[Has[AccessToken], SlackResponse[User]] =
    apiCall(Users.lookupByEmailUsers(LookupByEmailUsersRequest(email = emailId)).map(_.user))

  def deletePhoto: URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Users.deletePhotoUsers)

  def setPhoto(
    entity: RequestEntity,
    cropW: Option[Int] = None,
    cropX: Option[Int] = None,
    cropY: Option[Int] = None
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(
      request("users.setPhoto")
        .entityBody(entity)("crop_w" -> cropW, "crop_x" -> cropX, "crop_y" -> cropY)
    )

}

private[slack] trait UsersAccessors { _: Slack.type =>
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[GetPresenceUsersResponse]] =
    URIO.accessM(_.get.getUserPresence(userId))

  def getUserInfo(
    userId: String,
    includeLocale: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoUsersResponse]] =
    URIO.accessM(_.get.getUserInfo(userId, includeLocale))

  def listUsers(
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    includeLocale: Option[Boolean] = None,
    presence: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListUsersResponse]] =
    URIO.accessM(_.get.listUsers(cursor, limit, includeLocale, presence))

  def setUserActive(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.setUserActive())

  def setUserPresence(presence: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.setUserPresence(presence))

  def lookupUserByEmail(emailId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    URIO.accessM(_.get.lookupUserByEmail(emailId))

  def deletePhoto: URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.deletePhoto)

  def setPhoto(
    entity: RequestEntity,
    cropW: Option[Int] = None,
    cropX: Option[Int] = None,
    cropY: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.setPhoto(entity, cropW, cropX, cropY))

}

object Users extends GeneratedUsers
