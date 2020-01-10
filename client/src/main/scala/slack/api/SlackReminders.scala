package slack.api

import io.circe.Json
import io.circe.syntax._
import slack.models.Reminder
import slack._
import zio.ZIO

trait SlackReminders {
  val slackReminders: SlackReminders.Service[Any]
}

object SlackReminders {
  trait Service[R] {

    // TODO the time constraint can use special "natural language formats"
    //  Should consider using the zio.duration or a custom rolled DSL
    def addReminder(text: String, time: String, user: String): ZIO[R with SlackEnv, Throwable, Reminder] =
      sendM(
        requestJson("reminders.add",
                    Json.obj(
                      "text" -> text.asJson,
                      "time" -> time.asJson,
                      "user" -> user.asJson
                    ))
      ) >>= as[Reminder]("reminder")

    def completeReminder(reminder: String): ZIO[R with SlackEnv, Throwable, Boolean] =
      sendM(request("reminders.complete", "reminder" -> reminder)) >>= isOk

    def deleteReminder(reminder: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("reminders.delete", "reminder" -> reminder)) >>= isOk

    def getReminderInfo(reminder: String): ZIO[R with SlackEnv, SlackError, Reminder] =
      sendM(request("reminders.info", "reminder" -> reminder)) >>= as[Reminder]("reminder")

    def listReminders: ZIO[R with SlackEnv, SlackError, List[Reminder]] =
      sendM(request("reminders.list")) >>= as[List[Reminder]]("reminders")

  }
}

object reminders extends SlackReminders.Service[SlackEnv]
