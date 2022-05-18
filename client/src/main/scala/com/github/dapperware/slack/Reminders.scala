package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedReminders
import com.github.dapperware.slack.generated.requests.{
  CompleteRemindersRequest,
  DeleteRemindersRequest,
  InfoRemindersRequest
}
import com.github.dapperware.slack.generated.responses.ListRemindersResponse
import com.github.dapperware.slack.models.Reminder
import io.circe.Json
import zio.{ Has, URIO }

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

  // FIXME the arguments shouldn't be optional
  def completeReminder(reminder: String) =
    Reminders.completeReminders(CompleteRemindersRequest(Some(reminder))).toCall

  def deleteReminder(reminder: String) =
    Reminders.deleteReminders(DeleteRemindersRequest(Some(reminder))).toCall

  def getReminderInfo(reminder: String) =
    Reminders.infoReminders(InfoRemindersRequest(Some(reminder))).toCall

  def listReminders: URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListRemindersResponse]] =
    Reminders.listReminders.toCall

}

object Reminders extends GeneratedReminders
