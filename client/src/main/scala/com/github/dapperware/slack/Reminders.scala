package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.Reminder
import io.circe.Json

trait Reminders {

  def addReminder(text: String, time: String, user: String) =
    request("reminders.add")
      .jsonBody(
        Json.obj(
          "text" -> Json.fromString(text),
          "time" -> Json.fromString(time),
          "user" -> Json.fromString(user)
        )
      )
      .at[Reminder]("reminder")

  def completeReminder(reminder: String) =
    request("reminders.complete").formBody(Map("reminder" -> reminder))

  def deleteReminder(reminder: String)   =
    request("reminders.delete").formBody(Map("reminder" -> reminder))

  def getReminderInfo(reminder: String)  =
    request("reminders.info").formBody(Map("reminder" -> reminder)).at[Reminder]("reminder")

  def listReminders                      =
    request("reminders.list").at[List[Reminder]]("reminders")

}
