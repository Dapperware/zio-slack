package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.UserGroup

trait UserGroups {
  def createUserGroup(
    name: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None
  ) =
    request("usergroups.create")
      .formBody(
        "name"          -> name,
        "channels"      -> channels.mkString(","),
        "description"   -> description,
        "handle"        -> handle,
        "include_count" -> includeCount
      )
      .at[UserGroup]("usergroup")

  def setUserGroupEnabled(usergroup: String, enabled: Boolean, includeCount: Option[Boolean] = None) =
    request(if (enabled) "usergroups.enable" else "usergroups.disable")
      .formBody(
        "usergroup"     -> usergroup,
        "include_count" -> includeCount
      )
      .at[UserGroup]("usergroup")

  def listUserGroups(
    includeCount: Option[Boolean] = None,
    includeDisabled: Option[Boolean] = None,
    includeUsers: Option[Boolean] = None
  ) =
    request("usergroups.list")
      .formBody(
        "include_count"    -> includeCount,
        "include_disabled" -> includeDisabled,
        "include_users"    -> includeUsers
      )
      .at[List[UserGroup]]("usergroups")

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ) =
    request("usergroups.update")
      .formBody(
        "usergroup"     -> usergroup,
        "channels"      -> Some(channels).filter(_.nonEmpty).map(_.mkString(",")),
        "description"   -> description,
        "handle"        -> handle,
        "include_count" -> includeCount,
        "name"          -> name
      )
      .at[UserGroup]("usergroup")

  def listUserGroupUsers(usergroup: String, includeDisabled: Option[Boolean] = None) =
    request("usergroups.users.list")
      .formBody(
        "usergroup"        -> usergroup,
        "include_disabled" -> includeDisabled
      )
      .at[List[String]]("users")

  def updateUserGroupUsers(usergroup: String, users: List[String], includeCount: Option[Boolean] = None) =
    request("usergroups.users.update")
      .formBody(
        "usergroup"     -> usergroup,
        "users"         -> users.mkString(","),
        "include_count" -> includeCount
      )
      .at[UserGroup]("usergroup")
}
