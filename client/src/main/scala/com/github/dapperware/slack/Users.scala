package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.models.User
import zio.{ Has, URIO, ZIO }

trait Users {
  // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
  def getUserPresence(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[String]] =
    request("users.getPresence")
      .formBody(Map("user" -> userId))
      .at[String]("presence")
      .toCall

  def getUserInfo(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    request("users.info")
      .formBody(Map("user" -> userId))
      .at[User]("user")
      .toCall

  def listUsers(): URIO[Has[Slack] with Has[AccessToken], SlackResponse[List[User]]] =
    request("users.list")
      .at[List[User]]("members")
      .toCall

  def setUserActive(userId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("users.setActive")
      .formBody(Map("user" -> userId))
      .toCall

  def setUserPresence(presence: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]]  =
    request[Unit]("users.setPresence", "presence" -> presence).toCall

  def lookupUserByEmail(emailId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[User]] =
    request("users.lookupByEmail")
      .formBody(Map("email" -> emailId))
      .at[User]("user")
      .toCall

  def deletePhoto: URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("users.deletePhoto").toCall

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
