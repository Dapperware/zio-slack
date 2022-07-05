/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedChat {

  /**
   * Deletes a message.
   * @see https://api.slack.com/methods/chat.delete
   */
  def deleteChat(req: DeleteChatRequest): Request[DeleteChatResponse, AccessToken] =
    request("chat.delete").jsonBody(req).as[DeleteChatResponse].auth.accessToken

  /**
   * Deletes a pending scheduled message from the queue.
   * @see https://api.slack.com/methods/chat.deleteScheduledMessage
   */
  def deleteScheduledMessageChat(req: DeleteScheduledMessageChatRequest): Request[Unit, AccessToken] =
    request("chat.deleteScheduledMessage").jsonBody(req).auth.accessToken

  /**
   * Retrieve a permalink URL for a specific extant message
   * @see https://api.slack.com/methods/chat.getPermalink
   */
  def getPermalinkChat(req: GetPermalinkChatRequest): Request[GetPermalinkChatResponse, AccessToken] =
    request("chat.getPermalink").formBody(req).as[GetPermalinkChatResponse].auth.accessToken

  /**
   * Share a me message into a channel.
   * @see https://api.slack.com/methods/chat.meMessage
   */
  def meMessageChat(req: MeMessageChatRequest): Request[MeMessageChatResponse, AccessToken] =
    request("chat.meMessage").jsonBody(req).as[MeMessageChatResponse].auth.accessToken

  /**
   * Sends an ephemeral message to a user in a channel.
   * @see https://api.slack.com/methods/chat.postEphemeral
   */
  def postEphemeralChat(req: PostEphemeralChatRequest): Request[PostEphemeralChatResponse, AccessToken] =
    request("chat.postEphemeral").jsonBody(req).as[PostEphemeralChatResponse].auth.accessToken

  /**
   * Sends a message to a channel.
   * @see https://api.slack.com/methods/chat.postMessage
   */
  def postMessageChat(req: PostMessageChatRequest): Request[PostMessageChatResponse, AccessToken] =
    request("chat.postMessage").jsonBody(req).as[PostMessageChatResponse].auth.accessToken

  /**
   * Schedules a message to be sent to a channel.
   * @see https://api.slack.com/methods/chat.scheduleMessage
   */
  def scheduleMessageChat(req: ScheduleMessageChatRequest): Request[ScheduleMessageChatResponse, AccessToken] =
    request("chat.scheduleMessage").jsonBody(req).as[ScheduleMessageChatResponse].auth.accessToken

  /**
   * Returns a list of scheduled messages.
   * @see https://api.slack.com/methods/chat.scheduledMessages.list
   */
  def listScheduledMessagesChat(
    req: ListScheduledMessagesChatRequest
  ): Request[ListScheduledMessagesChatResponse, AccessToken] =
    request("chat.scheduledMessages.list").jsonBody(req).as[ListScheduledMessagesChatResponse].auth.accessToken

  /**
   * Provide custom unfurl behavior for user-posted URLs
   * @see https://api.slack.com/methods/chat.unfurl
   */
  def unfurlChat(req: UnfurlChatRequest): Request[Unit, AccessToken] =
    request("chat.unfurl").jsonBody(req).auth.accessToken

  /**
   * Updates a message.
   * @see https://api.slack.com/methods/chat.update
   */
  def updateChat(req: UpdateChatRequest): Request[UpdateChatResponse, AccessToken] =
    request("chat.update").jsonBody(req).as[UpdateChatResponse].auth.accessToken

}
