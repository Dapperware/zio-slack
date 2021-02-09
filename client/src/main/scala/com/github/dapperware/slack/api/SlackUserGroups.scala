package com.github.dapperware.slack.api

import com.github.dapperware.slack.models.UserGroup
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import io.circe.Decoder
import io.circe.generic.semiauto._
import zio.ZIO

import java.time.Instant

trait SlackUserGroups {

  def createUserGroup(name: String,
                      channels: List[String] = List.empty,
                      description: Option[String] = None,
                      handle: Option[String] = None,
                      includeCount: Option[Boolean] = None): ZIO[SlackEnv, SlackError, UserGroup] =
    sendM(
      request(
        "usergroups.create",
        "name"          -> name,
        "channels"      -> Some(channels).filter(_.nonEmpty).map(_.mkString(",")),
        "description"   -> description,
        "handle"        -> handle,
        "include_count" -> includeCount
      )
    ) >>= as[UserGroup]("usergroup")

  def setUserGroupEnabled(usergroup: String,
                          enabled: Boolean,
                          includeCount: Option[Boolean] = None): ZIO[SlackEnv, SlackError, UserGroup] =
    sendM(
      request(if (enabled) "usergroups.enable" else "usergroups.disable",
              "usergroup"     -> usergroup,
              "include_count" -> includeCount)
    ) >>= as[UserGroup]("usergroup")

  def listUserGroups(includeCount: Option[Boolean] = None,
                     includeDisabled: Option[Boolean] = None,
                     includeUsers: Option[Boolean] = None): ZIO[SlackEnv, SlackError, List[UserGroup]] =
    sendM(
      request(
        "usergroups.list",
        "include_count"    -> includeCount,
        "include_disabled" -> includeDisabled,
        "include_users"    -> includeUsers
      )
    ) >>= as[List[UserGroup]]("usergroups")

  def updateUserGroup(
    usergroup: String,
    channels: List[String] = List.empty,
    description: Option[String] = None,
    handle: Option[String] = None,
    includeCount: Option[Boolean] = None,
    name: Option[String] = None
  ): ZIO[SlackEnv, Throwable, UserGroup] =
    sendM(
      request(
        "usergroups.update",
        "usergroup"     -> usergroup,
        "channels"      -> Some(channels).filter(_.nonEmpty).map(_.mkString(",")),
        "description"   -> description,
        "handle"        -> handle,
        "include_count" -> includeCount,
        "name"          -> name
      )
    ) >>= as[UserGroup]("usergroup")

  def listUserGroupUsers(usergroup: String,
                         includeDisabled: Option[Boolean] = None): ZIO[SlackEnv, SlackError, List[String]] =
    sendM(request("usergroups.users.list", "usergroup" -> usergroup, "include_disabled" -> includeDisabled)) >>=
      as[List[String]]("users")

  def updateUserGroupUsers(usergroup: String,
                           users: List[String],
                           includeCount: Option[Boolean] = None): ZIO[SlackEnv, SlackError, UserGroup] =
    sendM(
      request("usergroups.users.update",
              "usergroup"     -> usergroup,
              "users"         -> users.mkString(","),
              "include_count" -> includeCount)
    ) >>= as[UserGroup]("usergroup")

}

object usergroups extends SlackUserGroups
