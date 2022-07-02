package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedReminders
import com.github.dapperware.slack.generated.requests.{
  CompleteRemindersRequest,
  DeleteRemindersRequest,
  InfoRemindersRequest
}
import com.github.dapperware.slack.generated.responses.{ InfoRemindersResponse, ListRemindersResponse }
import com.github.dapperware.slack.models.Reminder
import io.circe.Json
import zio.{ Has, URIO }

trait Reminders { self: Slack =>

  def addReminder(text: String, time: String, user: String): URIO[Has[AccessToken], SlackResponse[Reminder]] =
    apiCall(
      request("reminders.add")
        .jsonBody(
          Json.obj(
            "text" -> Json.fromString(text),
            "time" -> Json.fromString(time),
            "user" -> Json.fromString(user)
          )
        )
        .at[Reminder]("reminder")
    )

  // FIXME the arguments shouldn't be optional
  def completeReminder(reminder: String): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Reminders.completeReminders(CompleteRemindersRequest(Some(reminder))))

  def deleteReminder(reminder: String): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Reminders.deleteReminders(DeleteRemindersRequest(Some(reminder))))

  def getReminderInfo(reminder: String): URIO[Has[AccessToken], SlackResponse[InfoRemindersResponse]] =
    apiCall(Reminders.infoReminders(InfoRemindersRequest(Some(reminder))))

  def listReminders: URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListRemindersResponse]] =
    apiCall(Reminders.listReminders)

}

private[slack] trait RemindersAccessors { _: Slack.type =>

  def addReminder(
    text: String,
    time: String,
    user: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Reminder]] =
    URIO.accessM(_.get.addReminder(text, time, user))

  // FIXME the arguments shouldn't be optional
  def completeReminder(reminder: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.completeReminder(reminder))

  def deleteReminder(reminder: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.deleteReminder(reminder))

  def getReminderInfo(reminder: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[InfoRemindersResponse]] =
    URIO.accessM(_.get.getReminderInfo(reminder))

  def listReminders: URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListRemindersResponse]] =
    URIO.accessM(_.get.listReminders)

}

object Reminders extends GeneratedReminders
