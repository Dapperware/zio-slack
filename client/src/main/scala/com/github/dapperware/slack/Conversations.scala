package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.api.{ ChannelLike, ChannelLikeId }
import com.github.dapperware.slack.generated.GeneratedConversations
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses.{ CloseConversationsResponse, CreateConversationsResponse }
import com.github.dapperware.slack.models.{ Channel, HistoryChunk, Message, Plural, ResponseChunk }
import io.circe.Json
import io.circe.syntax._
import sttp.client3.IsOption
import zio.{ Has, URIO }

trait Conversations {

  /**
   * https://api.slack.com/methods/conversations.archive
   */
  def archiveConversation(channel: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Conversations.archiveConversations(ArchiveConversationsRequest(Some(channel))).toCall

  /**
   * https://api.slack.com/methods/conversations.close
   */
  def closeConversation(
    channelId: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[CloseConversationsResponse]] =
    Conversations.closeConversations(CloseConversationsRequest(Some(channelId))).toCall

  /**
   * https://api.slack.com/methods/conversations.create
   */
  // FIXME the arguments required for this are incorrect
  def createConversation(
    name: String,
    isPrivate: Option[Boolean] = None,
    userIds: Option[List[String]]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[CreateConversationsResponse]] =
    Conversations.createConversations(CreateConversationsRequest(Some(name), isPrivate)).toCall

  /**
   * https://api.slack.com/methods/conversations.history
   */
  def getConversationHistory(
    channelId: String,
    cursor: Option[String] = None,
    inclusive: Option[Boolean] = None,
    latest: Option[String] = None,
    limit: Option[Int] = None,
    oldest: Option[String] = None
  ) = {
    Conversations
      .historyConversations(
        HistoryConversationsRequest(
          channel = Some(channelId),
          cursor = cursor,
          inclusive = inclusive,
          latest = latest,
          limit = limit,
          oldest = oldest
        )
      )
      .toCall

    request("conversations.history")
      .formBody(
        "channel"   -> channelId,
        "cursor"    -> cursor,
        "inclusive" -> inclusive,
        "latest"    -> latest,
        "limit"     -> limit,
        "oldest"    -> oldest
      )
      .as[HistoryChunk]
  }

  def getSingleMessage(channelId: String, ts: String) =
    getConversationHistory(channelId, latest = Some(ts), inclusive = Some(true), limit = Some(1))
      .map(_.messages.headOption)

  /**
   * https://api.slack.com/methods/conversations.info
   */
  def getConversationInfo(
    channel: String,
    includeLocale: Option[Boolean] = None,
    includeNumMembers: Option[Boolean] = None
  ) =
    request("conversations.info")
      .formBody(
        "channel"         -> channel,
        "include_locale"  -> includeLocale,
        "include_num_mem" -> includeNumMembers
      )
      .at[Channel]("channel")

  /**
   * https://api.slack.com/methods/conversations.invite
   */
  def inviteToConversation(channel: String, users: List[String]) =
    Conversations.inviteConversations(InviteConversationsRequest(Some(channel), Some(users.mkString(",")))).toCall

  def inviteShareConversation(
    channel: String,
    users: Option[List[String]] = None,
    emails: Option[List[String]] = None,
    externalLimited: Option[Boolean] = None
  ) =
    request("conversations.invite")
      .formBody("channel" -> channel, "users_ids" -> users.map(_.mkString(",")))
      .at[Channel]("channel")

  /**
   * https://api.slack.com/methods/conversations.join
   */
  def joinConversation(channel: String) =
    Conversations.joinConversations(JoinConversationsRequest(Some(channel))).toCall

  /**
   * https://api.slack.com/methods/conversations.kick
   */
  def kickFromConversation(channel: String, user: String) =
    Conversations.kickConversations(KickConversationsRequest(Some(channel), Some(user))).toCall

  /**
   * https://api.slack.com/methods/conversations.leave
   */
  def leaveConversation(channel: String) =
    Conversations.leaveConversations(LeaveConversationsRequest(Some(channel))).toCall

  /**
   * https://api.slack.com/methods/conversations.list
   */
  def listConversations(
    cursor: Option[String] = None,
    excludeArchived: Option[Boolean] = None,
    limit: Option[Int] = None,
    types: Option[List[String]] = None
  ): Request[ResponseChunk[Channel], AccessToken] =
    Conversations
      .listConversations(
        ListConversationsRequest(
          exclude_archived = excludeArchived,
          cursor = cursor,
          limit = limit,
          types = types.map(_.mkString(","))
        )
      )
      .toCall

  /**
   * https://api.slack.com/methods/conversations.members
   */
  def getConversationMembers(channel: String, cursor: Option[String] = None, limit: Option[Int] = None) = {
    implicit val plural: Plural[String] = Plural.const("members")
    request("conversations.members")
      .formBody(
        "channel" -> channel,
        "cursor"  -> cursor,
        "limit"   -> limit
      )
      .as[ResponseChunk[String]]
  }

  /**
   * https://api.slack.com/methods/conversations.open
   */
  def openConversation[T](
    channel: Option[String] = None,
    returnIm: ChannelLike[T] = ChannelLikeId,
    users: Option[List[String]] = None
  ) =
    request("conversations.open")
      .jsonBody(
        Json.obj(
          "channel"   -> channel.asJson,
          "return_im" -> returnIm.isFull.asJson,
          "users"     -> users.map(_.mkString(",")).asJson
        )
      )
      .as(returnIm.decoder("channel"), IsOption.otherIsNotOption)

  /**
   * https://api.slack.com/methods/conversations.rename
   */
  def renameConversation(channel: String, name: String) =
    Conversations.renameConversations(RenameConversationsRequest(Some(channel), Some(name))).toCall

  /**
   * https://api.slack.com/methods/conversations.replies
   */
  def getConversationReplies(
    channel: String,
    ts: String,
    cursor: Option[String] = None,
    inclusive: Option[Boolean] = None,
    latest: Option[String] = None,
    limit: Option[Int] = None,
    oldest: Option[String] = None
  ) =
    request("conversations.replies")
      .formBody(
        "channel"   -> channel,
        "ts"        -> ts,
        "cursor"    -> cursor,
        "inclusive" -> inclusive,
        "latest"    -> latest,
        "limit"     -> limit,
        "oldest"    -> oldest
      )
      .as[ResponseChunk[Message]]

  /**
   * https://api.slack.com/methods/conversations.setPurpose
   */
  def setConversationPurpose(
    channel: String,
    purpose: String
  ) =
    Conversations.setPurposeConversations(SetPurposeConversationsRequest(Some(channel), Some(purpose))).toCall

  /**
   * https://api.slack.com/methods/conversations.setTopic
   */
  def setConversationTopic(
    channel: String,
    topic: String
  ) =
//    request("conversations.setTopic")
//      .jsonBody(Json.obj("channel" -> channel.asJson, "topic" -> topic.asJson))
//      .at[Conversation]("channel")
    Conversations.setTopicConversations(SetTopicConversationsRequest(Some(channel), Some(topic))).toCall

  /**
   * https://api.slack.com/methods/conversations.unarchive
   */
  def unarchiveConversation(
    channel: String
  ) =
    Conversations.unarchiveConversations(UnarchiveConversationsRequest(Some(channel))).toCall
}

object Conversations extends GeneratedConversations
