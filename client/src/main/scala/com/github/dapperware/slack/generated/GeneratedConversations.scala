/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedConversations {

  /**
   * Archives a conversation.
   * @see https://api.slack.com/methods/conversations.archive
   */
  def archiveConversations(req: ArchiveConversationsRequest): Request[Unit, AccessToken] =
    request("conversations.archive").jsonBody(req).auth.accessToken

  /**
   * Closes a direct message or multi-person direct message.
   * @see https://api.slack.com/methods/conversations.close
   */
  def closeConversations(req: CloseConversationsRequest): Request[CloseConversationsResponse, AccessToken] =
    request("conversations.close").jsonBody(req).as[CloseConversationsResponse].auth.accessToken

  /**
   * Initiates a public or private channel-based conversation
   * @see https://api.slack.com/methods/conversations.create
   */
  def createConversations(req: CreateConversationsRequest): Request[CreateConversationsResponse, AccessToken] =
    request("conversations.create").jsonBody(req).as[CreateConversationsResponse].auth.accessToken

  /**
   * Fetches a conversation's history of messages and events.
   * @see https://api.slack.com/methods/conversations.history
   */
  def historyConversations(req: HistoryConversationsRequest): Request[HistoryConversationsResponse, AccessToken] =
    request("conversations.history").formBody(req).as[HistoryConversationsResponse].auth.accessToken

  /**
   * Retrieve information about a conversation.
   * @see https://api.slack.com/methods/conversations.info
   */
  def infoConversations(req: InfoConversationsRequest): Request[InfoConversationsResponse, AccessToken] =
    request("conversations.info").formBody(req).as[InfoConversationsResponse].auth.accessToken

  /**
   * Invites users to a channel.
   * @see https://api.slack.com/methods/conversations.invite
   */
  def inviteConversations(req: InviteConversationsRequest): Request[InviteConversationsResponse, AccessToken] =
    request("conversations.invite").jsonBody(req).as[InviteConversationsResponse].auth.accessToken

  /**
   * Joins an existing conversation.
   * @see https://api.slack.com/methods/conversations.join
   */
  def joinConversations(req: JoinConversationsRequest): Request[JoinConversationsResponse, AccessToken] =
    request("conversations.join").jsonBody(req).as[JoinConversationsResponse].auth.accessToken

  /**
   * Removes a user from a conversation.
   * @see https://api.slack.com/methods/conversations.kick
   */
  def kickConversations(req: KickConversationsRequest): Request[Unit, AccessToken] =
    request("conversations.kick").jsonBody(req).auth.accessToken

  /**
   * Leaves a conversation.
   * @see https://api.slack.com/methods/conversations.leave
   */
  def leaveConversations(req: LeaveConversationsRequest): Request[LeaveConversationsResponse, AccessToken] =
    request("conversations.leave").jsonBody(req).as[LeaveConversationsResponse].auth.accessToken

  /**
   * Lists all channels in a Slack team.
   * @see https://api.slack.com/methods/conversations.list
   */
  def listConversations(req: ListConversationsRequest): Request[ListConversationsResponse, AccessToken] =
    request("conversations.list").formBody(req).as[ListConversationsResponse].auth.accessToken

  /**
   * Sets the read cursor in a channel.
   * @see https://api.slack.com/methods/conversations.mark
   */
  def markConversations(req: MarkConversationsRequest): Request[Unit, AccessToken] =
    request("conversations.mark").jsonBody(req).auth.accessToken

  /**
   * Retrieve members of a conversation.
   * @see https://api.slack.com/methods/conversations.members
   */
  def membersConversations(req: MembersConversationsRequest): Request[MembersConversationsResponse, AccessToken] =
    request("conversations.members").formBody(req).as[MembersConversationsResponse].auth.accessToken

  /**
   * Opens or resumes a direct message or multi-person direct message.
   * @see https://api.slack.com/methods/conversations.open
   */
  def openConversations(req: OpenConversationsRequest): Request[OpenConversationsResponse, AccessToken] =
    request("conversations.open").jsonBody(req).as[OpenConversationsResponse].auth.accessToken

  /**
   * Renames a conversation.
   * @see https://api.slack.com/methods/conversations.rename
   */
  def renameConversations(req: RenameConversationsRequest): Request[RenameConversationsResponse, AccessToken] =
    request("conversations.rename").jsonBody(req).as[RenameConversationsResponse].auth.accessToken

  /**
   * Retrieve a thread of messages posted to a conversation
   * @see https://api.slack.com/methods/conversations.replies
   */
  def repliesConversations(req: RepliesConversationsRequest): Request[RepliesConversationsResponse, AccessToken] =
    request("conversations.replies").formBody(req).as[RepliesConversationsResponse].auth.accessToken

  /**
   * Sets the purpose for a conversation.
   * @see https://api.slack.com/methods/conversations.setPurpose
   */
  def setPurposeConversations(
    req: SetPurposeConversationsRequest
  ): Request[SetPurposeConversationsResponse, AccessToken] =
    request("conversations.setPurpose").jsonBody(req).as[SetPurposeConversationsResponse].auth.accessToken

  /**
   * Sets the topic for a conversation.
   * @see https://api.slack.com/methods/conversations.setTopic
   */
  def setTopicConversations(req: SetTopicConversationsRequest): Request[SetTopicConversationsResponse, AccessToken] =
    request("conversations.setTopic").jsonBody(req).as[SetTopicConversationsResponse].auth.accessToken

  /**
   * Reverses conversation archival.
   * @see https://api.slack.com/methods/conversations.unarchive
   */
  def unarchiveConversations(req: UnarchiveConversationsRequest): Request[Unit, AccessToken] =
    request("conversations.unarchive").jsonBody(req).auth.accessToken

}
