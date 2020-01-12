package slack.api

import io.circe.Json
import io.circe.syntax._
import slack.models.UserProfile
import slack.{SlackEnv, SlackError}
import zio.ZIO

trait SlackProfile {
  val slackProfile: SlackProfile.Service[Any]
}

object SlackProfile {
  trait Service[R] {

    def getProfile(includeLabel: Boolean = false,
                   user: Option[String] = None): ZIO[R with SlackEnv, SlackError, UserProfile] =
      sendM(request("users.profile.get", "include_label" -> includeLabel, "user" -> user)) >>= as[UserProfile](
        "profile"
      )

    def setProfile(profile: Map[String, String],
                   user: Option[String] = None): ZIO[R with SlackEnv, SlackError, UserProfile] =
      sendM(
        requestJson("users.profile.set",
                    Json.obj(
                      "profile" -> profile.asJson,
                      "user"    -> user.asJson
                    ))
      ) >>= as[UserProfile]("profile")

    def setProfileValue(name: String,
                        value: String,
                        user: Option[String] = None): ZIO[R with SlackEnv, SlackError, UserProfile] =
      sendM(
        requestJson("users.profile.set",
                    Json.obj(
                      "name"  -> name.asJson,
                      "user"  -> user.asJson,
                      "value" -> value.asJson
                    ))
      ) >>= as[UserProfile]("profile")

  }
}

object profiles extends SlackProfile.Service[SlackEnv]
