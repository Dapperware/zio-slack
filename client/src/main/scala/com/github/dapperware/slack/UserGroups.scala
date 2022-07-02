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

trait UserGroups {
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[CreateUsergroupsResponse]] =
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
      .toCall

  def setUserGroupEnabled(
    usergroup: String,
    enabled: Boolean,
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
    URIO.effectSuspendTotal {
      (if (enabled)
         UserGroups
           .enableUsergroups(EnableUsergroupsRequest(usergroup, includeCount))
           .map(_.usergroup)
       else UserGroups.disableUsergroups(DisableUsergroupsRequest(usergroup, includeCount)).map(_.usergroup)).toCall
    }

  def listUserGroups(
    includeCount: Option[Boolean] = None,
    includeDisabled: Option[Boolean] = None,
    includeUsers: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Chunk[UserGroup]]] =
    UserGroups
      .listUsergroups(
        ListUsergroupsRequest(
          include_count = includeCount,
          include_disabled = includeDisabled,
          include_users = includeUsers
        )
      )
      .map(_.usergroups)
      .toCall

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
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
      .toCall

  def listUserGroupUsers(
    usergroup: String,
    includeDisabled: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListUsersUsergroupsResponse]] =
    UserGroups.listUsersUsergroups(ListUsersUsergroupsRequest(usergroup, includeDisabled)).toCall

  def updateUserGroupUsers(
    usergroup: String,
    users: List[String],
    includeCount: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserGroup]] =
    UserGroups
      .updateUsersUsergroups(
        UpdateUsersUsergroupsRequest(
          usergroup = usergroup,
          users = users.mkString(","),
          include_count = includeCount
        )
      )
      .map(_.usergroup)
      .toCall
}

object UserGroups extends GeneratedUsergroups
