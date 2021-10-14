package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import com.github.dapperware.slack.models._
import io.circe.Json
import io.circe.syntax._
import zio.ZIO

trait SlackReminders {

  // TODO the time constraint can use special "natural language formats"
  //  Should consider using the zio.duration or a custom rolled DSL
  def addReminder(text: String, time: String, user: String): ZIO[SlackEnv, SlackError, Reminder] =
    sendM(
      requestJson("reminders.add",
                  Json.obj(
                    "text" -> text.asJson,
                    "time" -> time.asJson,
                    "user" -> user.asJson
                  ))
    ) >>= as[Reminder]("reminder")

  def completeReminder(reminder: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("reminders.complete", "reminder" -> reminder)) >>= isOk

  def deleteReminder(reminder: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("reminders.delete", "reminder" -> reminder)) >>= isOk

  def getReminderInfo(reminder: String): ZIO[SlackEnv, SlackError, Reminder] =
    sendM(request("reminders.info", "reminder" -> reminder)) >>= as[Reminder]("reminder")

  def listReminders: ZIO[SlackEnv, SlackError, List[Reminder]] =
    sendM(request("reminders.list")) >>= as[List[Reminder]]("reminders")

}

object reminders extends SlackReminders
