package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedUsergroups
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses.{ CreateUsergroupsResponse, ListUsersUsergroupsResponse }
import com.github.dapperware.slack.models.UserGroup
import zio.{ Chunk, URIO, ZIO }

trait UserGroups { self: Slack =>
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[CreateUsergroupsResponse]] =
    apiCall(
      UserGroups
        .createUsergroups(
          CreateUsergroupsRequest(
            name = name,
            channels = Some(channels).filter(_.nonEmpty).map(_.mkString(",")),
            description = description,
            handle = handle,
            include_count = includeCount
          )
        )
    )

  def setUserGroupEnabled(
    usergroup: String,
    enabled: Boolean,
    includeCount: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[UserGroup]] =
    ZIO.suspendSucceed {
      apiCall(
        if (enabled)
          UserGroups
            .enableUsergroups(EnableUsergroupsRequest(usergroup, includeCount))
            .map(_.usergroup)
        else UserGroups.disableUsergroups(DisableUsergroupsRequest(usergroup, includeCount)).map(_.usergroup)
      )
    }

  def listUserGroups(
    includeCount: Option[Boolean] = None,
    includeDisabled: Option[Boolean] = None,
    includeUsers: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[Chunk[UserGroup]]] =
    apiCall(
      UserGroups
        .listUsergroups(
          ListUsergroupsRequest(
            include_count = includeCount,
            include_disabled = includeDisabled,
            include_users = includeUsers
          )
        )
        .map(_.usergroups)
    )

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ): URIO[AccessToken, SlackResponse[UserGroup]] =
    apiCall(
      UserGroups
        .updateUsergroups(
          UpdateUsergroupsRequest(
            usergroup = usergroup,
            channels = Some(channels).filter(_.nonEmpty).map(_.mkString(",")),
            description = description,
            handle = handle,
            include_count = includeCount,
            name = name
          )
        )
        .map(_.usergroup)
    )

  def listUserGroupUsers(
    usergroup: String,
    includeDisabled: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[ListUsersUsergroupsResponse]] =
    apiCall(UserGroups.listUsersUsergroups(ListUsersUsergroupsRequest(usergroup, includeDisabled)))

  def updateUserGroupUsers(
    usergroup: String,
    users: List[String],
    includeCount: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[UserGroup]] =
    apiCall(
      UserGroups
        .updateUsersUsergroups(
          UpdateUsersUsergroupsRequest(
            usergroup = usergroup,
            users = users.mkString(","),
            include_count = includeCount
          )
        )
        .map(_.usergroup)
    )
}

private[slack] trait UserGroupsAccessors { self: Slack.type =>
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[CreateUsergroupsResponse]] =
    ZIO.serviceWithZIO[Slack](_.createUserGroup(name, channels, description, handle, includeCount))

  def setUserGroupEnabled(
    usergroup: String,
    enabled: Boolean,
    includeCount: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[UserGroup]] =
    ZIO.serviceWithZIO[Slack](_.setUserGroupEnabled(usergroup, enabled, includeCount))

  def listUserGroups(
    includeCount: Option[Boolean] = None,
    includeDisabled: Option[Boolean] = None,
    includeUsers: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[Chunk[UserGroup]]] =
    ZIO.serviceWithZIO[Slack](_.listUserGroups(includeCount, includeDisabled, includeUsers))

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ): URIO[Slack with AccessToken, SlackResponse[UserGroup]] =
    ZIO.serviceWithZIO[Slack](_.updateUserGroup(usergroup, channels, description, handle, includeCount, name))

  def listUserGroupUsers(
    usergroup: String,
    includeDisabled: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[ListUsersUsergroupsResponse]] =
    ZIO.serviceWithZIO[Slack](_.listUserGroupUsers(usergroup, includeDisabled))

  def updateUserGroupUsers(
    usergroup: String,
    users: List[String],
    includeCount: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[UserGroup]] =
    ZIO.serviceWithZIO[Slack](_.updateUserGroupUsers(usergroup, users, includeCount))
}

object UserGroups extends GeneratedUsergroups
