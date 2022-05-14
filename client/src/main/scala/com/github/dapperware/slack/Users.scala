package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedRequest
import com.github.dapperware.slack.models.User
import zio.{Has, URIO, ZIO}

trait Users {
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[String]] =
    Request.make("users.getPresence").formBody(Map("user" -> userId))
      .at[String]("presence")
      .toCall

  def getUserInfo(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    Request.make("users.info").formBody(Map("user" -> userId))
      .at[User]("user")
      .toCall

  def listUsers(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[List[User]]] =
    Request.make("users.list")
      .at[List[User]]("members")
      .toCall

  def setUserActive(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request.make("users.setActive")
      .formBody(Map("user" -> userId))
      .toCall

  def setUserPresence(presence: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request.make("users.setPresence")
      .formBody(Map("presence" -> presence))
      .toCall

  def lookupUserByEmail(emailId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    Request.make("users.lookupByEmail")
      .formBody(Map("email" -> emailId))
      .at[User]("user")
      .toCall

}
