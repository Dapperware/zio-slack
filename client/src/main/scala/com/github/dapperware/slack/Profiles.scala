package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.UserProfile
import io.circe.Json
import io.circe.syntax._

trait Profiles {
  def getProfile(includeLabel: Boolean = false, user: Option[String] = None) =
    request("users.profile.get")
      .formBody(
        "include_labels" -> includeLabel.toString,
        "user"           -> user
      )
      .at[UserProfile]("profile")

  def setProfile(profile: Map[String, String], user: Option[String] = None) =
    request("users.profile.set")
      .jsonBody(
        Json.obj(
          "profile" -> profile.asJson,
          "user"    -> user.asJson
        )
      )
      .at[UserProfile]("profile")

  def setProfileValue(name: String, value: String, user: Option[String] = None) =
    request("users.profile.set")
      .jsonBody(
        Json.obj(
          "name"  -> name.asJson,
          "value" -> value.asJson,
          "user"  -> user.asJson
        )
      )
      .at[UserProfile]("profile")
}
