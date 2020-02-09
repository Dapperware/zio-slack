package slack.api

import java.time.Instant

import io.circe.Decoder
import io.circe.generic.semiauto._
import slack.{ SlackEnv, SlackError }
import zio.ZIO

trait SlackUserGroups {
  val slackUserGroups: SlackUserGroups.Service
}

object SlackUserGroups {
  trait Service {

    def createUserGroup(name: String,
                        channels: List[String] = List.empty,
                        description: Option[String] = None,
                        handle: Option[String] = None,
                        includeCount: Option[Boolean] = None): ZIO[SlackEnv, Throwable, UserGroup] =
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
                            includeCount: Option[Boolean] = None): ZIO[SlackEnv, Throwable, UserGroup] =
      sendM(
        request(if (enabled) "usergroups.enable" else "usergroups.disable",
                "usergroup"     -> usergroup,
                "include_count" -> includeCount)
      ) >>= as[UserGroup]("usergroup")

    def listUserGroups(includeCount: Option[Boolean] = None,
                       includeDisabled: Option[Boolean] = None,
                       includeUsers: Option[Boolean] = None): ZIO[SlackEnv, Throwable, List[UserGroup]] =
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
}

object usergroups extends SlackUserGroups.Service

case class UserGroup(
  id: String,
  team_id: String,
  is_usergroup: Boolean,
  is_external: Option[Boolean],
  name: Option[String],
  description: Option[String],
  handle: Option[String],
  date_create: Instant,
  date_update: Option[Instant],
  date_delete: Option[Instant],
  deleted_by: Option[String],
  auto_type: Option[String],
  users: Option[List[String]],
  user_count: Option[String] // TODO map to Int
)

case class UserPrefs(
  channels: List[String],
  groups: List[String]
)

object UserGroup {
  implicit val prefsDecoder: Decoder[UserPrefs] =
    deriveDecoder[UserPrefs]

  implicit val decoder: Decoder[UserGroup] =
    deriveDecoder[UserGroup]
}
