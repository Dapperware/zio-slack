package slack.api

import slack.models.User
import slack._
import zio.ZIO
import zio.macros.annotation.mockable

@mockable
trait SlackUsers {
  val slackUsers: SlackUsers.Service[Any]
}

object SlackUsers {
  trait Service[R] {
    // TODO: Full payload for authed user: https://api.slack.com/methods/users.getPresence
    def getUserPresence(userId: String): ZIO[R with SlackEnv, SlackError, String] =
      sendM(request("user.getPresence", "user" -> userId)) >>= as[String]("presence")

    def getUserInfo(userId: String): ZIO[R with SlackEnv, SlackError, User] =
      sendM(request("users.info", "user" -> userId)) >>= as[User]("user")

    def listUsers(): ZIO[R with SlackEnv, SlackError, Seq[User]] =
      sendM(request("users.list")) >>= as[Seq[User]]("members")

    def setUserActive(userId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("users.setActive", "user" -> userId)) >>= isOk

    def setUserPresence(presence: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("users.setPresence", "presence" -> presence)) >>= isOk

    def lookupUserByEmail(emailId: String): ZIO[R with SlackEnv, SlackError, User] =
      sendM(request("users.lookupByEmail", "email" -> emailId)) >>= as[User]("user")
  }

}

object users extends SlackUsers.Service[SlackEnv]
