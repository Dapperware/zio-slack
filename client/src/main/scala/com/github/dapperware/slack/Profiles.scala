package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.UserProfile
import io.circe.Json
import io.circe.syntax._
import zio.{ Has, URIO }

trait Profiles { self: Slack =>
  def getProfile(
    includeLabel: Boolean = false,
    user: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.get")
        .formBody(
          "include_labels" -> includeLabel.toString,
          "user"           -> user
        )
        .at[UserProfile]("profile")
    )

  def setProfile(
    profile: Map[String, String],
    user: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.set")
        .jsonBody(
          Json.obj(
            "profile" -> profile.asJson,
            "user"    -> user.asJson
          )
        )
        .at[UserProfile]("profile")
    )

  def setProfileValue(
    name: String,
    value: String,
    user: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.set")
        .jsonBody(
          Json.obj(
            "name"  -> name.asJson,
            "value" -> value.asJson,
            "user"  -> user.asJson
          )
        )
        .at[UserProfile]("profile")
    )
}

private[slack] trait ProfilesAccessors { self: Slack.type =>
  def getProfile(
    includeLabel: Boolean = false,
    user: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserProfile]] =
    URIO.accessM(_.get.getProfile(includeLabel, user))

  def setProfile(
    profile: Map[String, String],
    user: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserProfile]] =
    URIO.accessM(_.get.setProfile(profile, user))

  def setProfileValue(
    name: String,
    value: String,
    user: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UserProfile]] =
    URIO.accessM(_.get.setProfileValue(name, value, user))
}
