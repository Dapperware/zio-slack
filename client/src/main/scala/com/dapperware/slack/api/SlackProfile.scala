package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import com.dapperware.slack.models.UserProfile
import io.circe.Json
import io.circe.syntax._
import zio.ZIO

trait SlackProfile {

  def getProfile(includeLabel: Boolean = false, user: Option[String] = None): ZIO[SlackEnv, SlackError, UserProfile] =
    sendM(request("users.profile.get", "include_label" -> includeLabel, "user" -> user)) >>= as[UserProfile](
      "profile"
    )

  def setProfile(profile: Map[String, String], user: Option[String] = None): ZIO[SlackEnv, SlackError, UserProfile] =
    sendM(
      requestJson("users.profile.set",
                  Json.obj(
                    "profile" -> profile.asJson,
                    "user"    -> user.asJson
                  ))
    ) >>= as[UserProfile]("profile")

  def setProfileValue(name: String,
                      value: String,
                      user: Option[String] = None): ZIO[SlackEnv, SlackError, UserProfile] =
    sendM(
      requestJson("users.profile.set",
                  Json.obj(
                    "name"  -> name.asJson,
                    "user"  -> user.asJson,
                    "value" -> value.asJson
                  ))
    ) >>= as[UserProfile]("profile")

}

object profiles extends SlackProfile
