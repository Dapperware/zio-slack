package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedUsergroups
import com.github.dapperware.slack.generated.requests.{
  CreateUsergroupsRequest,
  DisableUsergroupsRequest,
  EnableUsergroupsRequest,
  ListUsergroupsRequest,
  ListUsersUsergroupsRequest,
  UpdateUsergroupsRequest,
  UpdateUsersUsergroupsRequest
}
import com.github.dapperware.slack.generated.responses.{ CreateUsergroupsResponse, ListUsersUsergroupsResponse }
import com.github.dapperware.slack.models.UserGroup
import zio.{ Chunk, Has, URIO }

trait UserGroups { self: Slack =>
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ): URIO[Has[AccessToken], SlackResponse[CreateUsergroupsResponse]] =
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
  ): URIO[Has[AccessToken], SlackResponse[UserGroup]] =
    URIO.effectSuspendTotal {
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
  ): URIO[Has[AccessToken], SlackResponse[Chunk[UserGroup]]] =
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
  ): URIO[Has[AccessToken], SlackResponse[UserGroup]] =
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
  ): URIO[Has[AccessToken], SlackResponse[ListUsersUsergroupsResponse]] =
    apiCall(UserGroups.listUsersUsergroups(ListUsersUsergroupsRequest(usergroup, includeDisabled)))

  def updateUserGroupUsers(
    usergroup: String,
    users: List[String],
    includeCount: Option[Boolean] = None
  ): URIO[Has[AccessToken], SlackResponse[UserGroup]] =
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

private[slack] trait UserGroupsAccessors { _: Slack.type =>
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[CreateUsergroupsResponse]] =
    URIO.accessM(_.get.createUserGroup(name, channels, description, handle, includeCount))

  def setUserGroupEnabled(
    usergroup: String,
    enabled: Boolean,
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
    URIO.accessM(_.get.setUserGroupEnabled(usergroup, enabled, includeCount))

  def listUserGroups(
    includeCount: Option[Boolean] = None,
    includeDisabled: Option[Boolean] = None,
    includeUsers: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Chunk[UserGroup]]] =
    URIO.accessM(_.get.listUserGroups(includeCount, includeDisabled, includeUsers))

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
    URIO.accessM(_.get.updateUserGroup(usergroup, channels, description, handle, includeCount, name))

  def listUserGroupUsers(
    usergroup: String,
    includeDisabled: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListUsersUsergroupsResponse]] =
    URIO.accessM(_.get.listUserGroupUsers(usergroup, includeDisabled))

  def updateUserGroupUsers(
    usergroup: String,
    users: List[String],
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
    URIO.accessM(_.get.updateUserGroupUsers(usergroup, users, includeCount))
}

object UserGroups extends GeneratedUsergroups
