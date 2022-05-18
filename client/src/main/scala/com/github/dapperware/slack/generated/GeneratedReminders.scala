/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedReminders {

  /**
   * Marks a reminder as complete.
   * @see https://api.slack.com/methods/reminders.complete
   */
  def completeReminders(req: CompleteRemindersRequest): Request[Unit, AccessToken] =
    request("reminders.complete").jsonBody(req).auth.accessToken

  /**
   * Deletes a reminder.
   * @see https://api.slack.com/methods/reminders.delete
   */
  def deleteReminders(req: DeleteRemindersRequest): Request[Unit, AccessToken] =
    request("reminders.delete").jsonBody(req).auth.accessToken

  /**
   * Gets information about a reminder.
   * @see https://api.slack.com/methods/reminders.info
   */
  def infoReminders(req: InfoRemindersRequest): Request[InfoRemindersResponse, AccessToken] =
    request("reminders.info").formBody(req).as[InfoRemindersResponse].auth.accessToken

  /**
   * Lists all reminders created by or for a given user.
   * @see https://api.slack.com/methods/reminders.list
   */
  def listReminders: Request[ListRemindersResponse, AccessToken] =
    request("reminders.list").as[ListRemindersResponse].auth.accessToken

}
