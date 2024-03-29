package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.generated.GeneratedUsers
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses.{
  GetPresenceUsersResponse,
  InfoUsersResponse,
  ListUsersResponse
}
import com.github.dapperware.slack.models.User
import zio.{ Trace, URIO, ZIO }

trait Users { self: SlackApiBase =>
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String)(implicit
    trace: Trace
  ): URIO[AccessToken, SlackResponse[GetPresenceUsersResponse]] =
    apiCall(Users.getPresenceUsers(GetPresenceUsersRequest(Some(userId))))

  def getUserInfo(
    userId: String,
    includeLocale: Option[Boolean] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[InfoUsersResponse]] =
    apiCall(Users.infoUsers(InfoUsersRequest(user = Some(userId), include_locale = includeLocale)))

  def listUsers(
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    includeLocale: Option[Boolean] = None,
    presence: Option[Boolean] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[ListUsersResponse]] =
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

  def setUserActive()(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Users.setActiveUsers)

  def setUserPresence(presence: String)(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Users.setPresenceUsers(SetPresenceUsersRequest(presence)))

  def lookupUserByEmail(emailId: String)(implicit trace: Trace): URIO[AccessToken, SlackResponse[User]] =
    apiCall(Users.lookupByEmailUsers(LookupByEmailUsersRequest(email = emailId)).map(_.user))

  def deletePhoto: URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Users.deletePhotoUsers)

  def setPhoto(
    entity: RequestEntity,
    cropW: Option[Int] = None,
    cropX: Option[Int] = None,
    cropY: Option[Int] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(
      request("users.setPhoto")
        .entityBody(entity)("crop_w" -> cropW, "crop_x" -> cropX, "crop_y" -> cropY)
        .auth
        .accessToken
    )

}

private[slack] trait UsersAccessors { self: Slack.type =>
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String)(implicit
    trace: Trace
  ): URIO[Slack with AccessToken, SlackResponse[GetPresenceUsersResponse]] =
    ZIO.serviceWithZIO[Slack](_.getUserPresence(userId))

  def getUserInfo(
    userId: String,
    includeLocale: Option[Boolean] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[InfoUsersResponse]] =
    ZIO.serviceWithZIO[Slack](_.getUserInfo(userId, includeLocale))

  def listUsers(
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    includeLocale: Option[Boolean] = None,
    presence: Option[Boolean] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[ListUsersResponse]] =
    ZIO.serviceWithZIO[Slack](_.listUsers(cursor, limit, includeLocale, presence))

  def setUserActive()(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.setUserActive())

  def setUserPresence(presence: String)(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.setUserPresence(presence))

  def lookupUserByEmail(emailId: String)(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[User]] =
    ZIO.serviceWithZIO[Slack](_.lookupUserByEmail(emailId))

  def deletePhoto: URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.deletePhoto)

  def setPhoto(
    entity: RequestEntity,
    cropW: Option[Int] = None,
    cropX: Option[Int] = None,
    cropY: Option[Int] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.setPhoto(entity, cropW, cropX, cropY))

}

object Users extends GeneratedUsers
