package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.UserProfile
import io.circe.Json
import io.circe.syntax._
import zio.{Trace, URIO, ZIO}

trait Profiles { self: SlackApiBase =>
  def getProfile(
    includeLabel: Boolean = false,
    user: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.get")
        .formBody(
          "include_labels" -> includeLabel.toString,
          "user"           -> user
        )
        .jsonAt[UserProfile]("profile")
    )

  def setProfile(
    profile: Map[String, String],
    user: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.set")
        .jsonBody(
          Json.obj(
            "profile" -> profile.asJson,
            "user"    -> user.asJson
          )
        )
        .jsonAt[UserProfile]("profile")
    )

  def setProfileValue(
    name: String,
    value: String,
    user: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[UserProfile]] =
    apiCall(
      request("users.profile.set")
        .jsonBody(
          Json.obj(
            "name"  -> name.asJson,
            "value" -> value.asJson,
            "user"  -> user.asJson
          )
        )
        .jsonAt[UserProfile]("profile")
    )
}

private[slack] trait ProfilesAccessors { self: Slack.type =>
  def getProfile(
    includeLabel: Boolean = false,
    user: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[UserProfile]] =
    ZIO.serviceWithZIO[Slack](_.getProfile(includeLabel, user))

  def setProfile(
    profile: Map[String, String],
    user: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[UserProfile]] =
    ZIO.serviceWithZIO[Slack](_.setProfile(profile, user))

  def setProfileValue(
    name: String,
    value: String,
    user: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[UserProfile]] =
    ZIO.serviceWithZIO[Slack](_.setProfileValue(name, value, user))
}
