package slack.api

import io.circe.Decoder
import io.circe.generic.semiauto._
import slack._
import zio.ZIO

trait SlackDnd {
  val slackDnd: SlackDnd.Service[Any]
}

object SlackDnd {
  trait Service[R] {

    def endDnd(): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("dnd.endDnd")) >>= isOk

    def endSnooze(): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("dnd.endSnooze")) >>= isOk

    def getDoNotDisturbInfo(userId: Option[String] = None): ZIO[R with SlackEnv, SlackError, DndInfo] =
      sendM(request("dnd.info")) >>= as[DndInfo]

    def setSnooze(numMinutes: Int): ZIO[R with SlackEnv, SlackError, SnoozeInfo] =
      sendM(request("dnd.setSnooze", "num_minutes" -> numMinutes)) >>= as[SnoozeInfo]

    def getTeamDoNotDisturbInfo(users: List[String]): ZIO[R with SlackEnv, SlackError, Map[String, DndInfo]] =
      sendM(request("dnd.teamInfo", "users" -> users.mkString(","))) >>= as[Map[String, DndInfo]]("users")
  }
}

object dnd extends SlackDnd.Service[SlackEnv]

case class SnoozeInfo(
  snoozeEnabled: Boolean,
  nextSnoozeStartTs: Long,
  nextSnoozeEndTs: Long
)

object SnoozeInfo {
  implicit val decoder: Decoder[SnoozeInfo] = deriveDecoder[SnoozeInfo]
}

case class DndInfo(
  dndEnabled: Boolean,
  nextDndStartTs: Long,
  nextDndEndTs: Long
)

object DndInfo {
  implicit val decoder: Decoder[DndInfo] = deriveDecoder[DndInfo]
}
