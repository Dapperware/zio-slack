package com.dapperware.slack.api

import com.dapperware.slack.models.User
import com.dapperware.slack.{SlackEnv, SlackError}
import zio.ZIO

trait SlackUsers {
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): ZIO[SlackEnv, SlackError, String] =
    sendM(request("user.getPresence", "user" -> userId)) >>= as[String]("presence")

  def getUserInfo(userId: String): ZIO[SlackEnv, SlackError, User] =
    sendM(request("users.info", "user" -> userId)) >>= as[User]("user")

  def listUsers(): ZIO[SlackEnv, SlackError, Seq[User]] =
    sendM(request("users.list")) >>= as[Seq[User]]("members")

  def setUserActive(userId: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("users.setActive", "user" -> userId)) >>= isOk

  def setUserPresence(presence: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("users.setPresence", "presence" -> presence)) >>= isOk

  def lookupUserByEmail(emailId: String): ZIO[SlackEnv, SlackError, User] =
    sendM(request("users.lookupByEmail", "email" -> emailId)) >>= as[User]("user")
}

object users extends SlackUsers
