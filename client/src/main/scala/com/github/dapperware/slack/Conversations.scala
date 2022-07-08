package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedConversations
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.models.{ Channel, ChannelLike, ChannelLikeId, Message, Plural, ResponseChunk }
import io.circe.Json
import io.circe.syntax._
import sttp.client3.IsOption
import zio.{ URIO, ZIO }

trait Conversations { self: Slack =>

  /**
   * https://api.slack.com/methods/conversations.archive
   */
  def archiveConversation(channel: String): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Conversations.archiveConversations(ArchiveConversationsRequest(Some(channel))))

  /**
   * https://api.slack.com/methods/conversations.close
   */
  def closeConversation(
    channelId: String
  ): URIO[AccessToken, SlackResponse[CloseConversationsResponse]] =
    apiCall(Conversations.closeConversations(CloseConversationsRequest(Some(channelId))))

  /**
   * https://api.slack.com/methods/conversations.create
   */
  // FIXME the arguments required for this are incorrect
  def createConversation(
    name: String,
    isPrivate: Option[Boolean] = None,
    userIds: Option[List[String]]
  ): URIO[AccessToken, SlackResponse[CreateConversationsResponse]] =
    apiCall(Conversations.createConversations(CreateConversationsRequest(Some(name), isPrivate)))

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
  ): URIO[AccessToken, SlackResponse[HistoryConversationsResponse]] =
    apiCall(
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
    )

  def getSingleMessage(channelId: String, ts: String): ZIO[AccessToken, Nothing, SlackResponse[Option[Message]]] =
    getConversationHistory(channelId, latest = Some(ts), inclusive = Some(true), limit = Some(1))
      .map(_.map(_.messages.headOption))

  /**
   * https://api.slack.com/methods/conversations.info
   */
  def getConversationInfo(
    channel: String,
    includeLocale: Option[Boolean] = None,
    includeNumMembers: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[Channel]] =
    apiCall(
      request("conversations.info")
        .formBody(
          "channel"         -> channel,
          "include_locale"  -> includeLocale,
          "include_num_mem" -> includeNumMembers
        )
        .at[Channel]("channel")
    )

  /**
   * https://api.slack.com/methods/conversations.invite
   */
  def inviteToConversation(
    channel: String,
    users: List[String]
  ): URIO[AccessToken, SlackResponse[InviteConversationsResponse]] =
    apiCall(Conversations.inviteConversations(InviteConversationsRequest(Some(channel), Some(users.mkString(",")))))

  def inviteShareConversation(
    channel: String,
    users: Option[List[String]] = None,
    emails: Option[List[String]] = None,
    externalLimited: Option[Boolean] = None
  ): URIO[AccessToken, SlackResponse[Channel]] =
    apiCall(
      request("conversations.invite")
        .formBody("channel" -> channel, "users_ids" -> users.map(_.mkString(",")))
        .at[Channel]("channel")
    )

  /**
   * https://api.slack.com/methods/conversations.join
   */
  def joinConversation(channel: String): URIO[AccessToken, SlackResponse[JoinConversationsResponse]] =
    apiCall(Conversations.joinConversations(JoinConversationsRequest(Some(channel))))

  /**
   * https://api.slack.com/methods/conversations.kick
   */
  def kickFromConversation(channel: String, user: String): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Conversations.kickConversations(KickConversationsRequest(Some(channel), Some(user))))

  /**
   * https://api.slack.com/methods/conversations.leave
   */
  def leaveConversation(channel: String): URIO[AccessToken, SlackResponse[LeaveConversationsResponse]] =
    apiCall(Conversations.leaveConversations(LeaveConversationsRequest(Some(channel))))

  /**
   * https://api.slack.com/methods/conversations.list
   */
  def listConversations(
    cursor: Option[String] = None,
    excludeArchived: Option[Boolean] = None,
    limit: Option[Int] = None,
    types: Option[List[String]] = None
  ): URIO[AccessToken, SlackResponse[ListConversationsResponse]] =
    apiCall(
      Conversations
        .listConversations(
          ListConversationsRequest(
            exclude_archived = excludeArchived,
            cursor = cursor,
            limit = limit,
            types = types.map(_.mkString(","))
          )
        )
    )

  /**
   * https://api.slack.com/methods/conversations.members
   */
  def getConversationMembers(
    channel: String,
    cursor: Option[String] = None,
    limit: Option[Int] = None
  ): URIO[AccessToken, SlackResponse[ResponseChunk[String]]] = {
    implicit val plural: Plural[String] = Plural.const("members")
    apiCall(
      request("conversations.members")
        .formBody(
          "channel" -> channel,
          "cursor"  -> cursor,
          "limit"   -> limit
        )
        .as[ResponseChunk[String]]
    )
  }

  /**
   * https://api.slack.com/methods/conversations.open
   */
  def openConversation[T](
    channel: Option[String] = None,
    returnIm: ChannelLike[T] = ChannelLikeId,
    users: Option[List[String]] = None
  ): URIO[AccessToken, SlackResponse[returnIm.ChannelType]] =
    apiCall(
      request("conversations.open")
        .jsonBody(
          Json.obj(
            "channel"   -> channel.asJson,
            "return_im" -> returnIm.isFull.asJson,
            "users"     -> users.map(_.mkString(",")).asJson
          )
        )
        .as(returnIm.decoder("channel"), IsOption.otherIsNotOption)
    )

  /**
   * https://api.slack.com/methods/conversations.rename
   */
  def renameConversation(
    channel: String,
    name: String
  ): URIO[AccessToken, SlackResponse[RenameConversationsResponse]] =
    apiCall(Conversations.renameConversations(RenameConversationsRequest(Some(channel), Some(name))))

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
  ): URIO[AccessToken, SlackResponse[ResponseChunk[Message]]] =
    apiCall(
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
    )

  /**
   * https://api.slack.com/methods/conversations.setPurpose
   */
  def setConversationPurpose(
    channel: String,
    purpose: String
  ): URIO[AccessToken, SlackResponse[SetPurposeConversationsResponse]] =
    apiCall(Conversations.setPurposeConversations(SetPurposeConversationsRequest(Some(channel), Some(purpose))))

  /**
   * https://api.slack.com/methods/conversations.setTopic
   */
  def setConversationTopic(
    channel: String,
    topic: String
  ): URIO[AccessToken, SlackResponse[SetTopicConversationsResponse]] =
//    request("conversations.setTopic")
//      .jsonBody(Json.obj("channel" -> channel.asJson, "topic" -> topic.asJson))
//      .at[Conversation]("channel")
    apiCall(Conversations.setTopicConversations(SetTopicConversationsRequest(Some(channel), Some(topic))))

  /**
   * https://api.slack.com/methods/conversations.unarchive
   */
  def unarchiveConversation(
    channel: String
  ): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Conversations.unarchiveConversations(UnarchiveConversationsRequest(Some(channel))))
}

private[slack] trait ConversationsAccessors { _: Slack.type =>

  /**
   * https://api.slack.com/methods/conversations.archive
   */
  def archiveConversation(channel: String): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.archiveConversation(channel))

  /**
   * https://api.slack.com/methods/conversations.close
   */
  def closeConversation(
    channelId: String
  ): URIO[Slack with AccessToken, SlackResponse[CloseConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.closeConversation(channelId))

  /**
   * https://api.slack.com/methods/conversations.create
   */
  // FIXME the arguments required for this are incorrect
  def createConversation(
    name: String,
    isPrivate: Option[Boolean] = None,
    userIds: Option[List[String]]
  ): URIO[Slack with AccessToken, SlackResponse[CreateConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.createConversation(name, isPrivate, userIds))

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
  ): ZIO[Slack with AccessToken, Nothing, SlackResponse[HistoryConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.getConversationHistory(channelId, cursor, inclusive, latest, limit, oldest))

  def getSingleMessage(
    channelId: String,
    ts: String
  ): ZIO[Slack with AccessToken, Nothing, SlackResponse[Option[Message]]] =
    ZIO.serviceWithZIO[Slack](_.getSingleMessage(channelId, ts))

  /**
   * https://api.slack.com/methods/conversations.info
   */
  def getConversationInfo(
    channel: String,
    includeLocale: Option[Boolean] = None,
    includeNumMembers: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[Channel]] =
    ZIO.serviceWithZIO[Slack](_.getConversationInfo(channel, includeLocale, includeNumMembers))

  /**
   * https://api.slack.com/methods/conversations.invite
   */
  def inviteToConversation(
    channel: String,
    users: List[String]
  ): ZIO[Slack with AccessToken, Nothing, SlackResponse[InviteConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.inviteToConversation(channel, users))

  def inviteShareConversation(
    channel: String,
    users: Option[List[String]] = None,
    emails: Option[List[String]] = None,
    externalLimited: Option[Boolean] = None
  ): URIO[Slack with AccessToken, SlackResponse[Channel]] =
    ZIO.serviceWithZIO[Slack](_.inviteShareConversation(channel, users, emails, externalLimited))

  /**
   * https://api.slack.com/methods/conversations.join
   */
  def joinConversation(
    channel: String
  ): URIO[Slack with AccessToken, SlackResponse[JoinConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.joinConversation(channel))

  /**
   * https://api.slack.com/methods/conversations.kick
   */
  def kickFromConversation(channel: String, user: String): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.kickFromConversation(channel, user))

  /**
   * https://api.slack.com/methods/conversations.leave
   */
  def leaveConversation(
    channel: String
  ): URIO[Slack with AccessToken, SlackResponse[LeaveConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.leaveConversation(channel))

  /**
   * https://api.slack.com/methods/conversations.list
   */
  def listConversations(
    cursor: Option[String] = None,
    excludeArchived: Option[Boolean] = None,
    limit: Option[Int] = None,
    types: Option[List[String]] = None
  ): URIO[Slack with AccessToken, SlackResponse[ListConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.listConversations(cursor, excludeArchived, limit, types))

  /**
   * https://api.slack.com/methods/conversations.members
   */
  def getConversationMembers(
    channel: String,
    cursor: Option[String] = None,
    limit: Option[Int] = None
  ): URIO[Slack with AccessToken, SlackResponse[ResponseChunk[String]]] =
    ZIO.serviceWithZIO[Slack](_.getConversationMembers(channel, cursor, limit))

  /**
   * https://api.slack.com/methods/conversations.open
   */
  def openConversation[T](
    channel: Option[String] = None,
    returnIm: ChannelLike[T] = ChannelLikeId,
    users: Option[List[String]] = None
  ): URIO[Slack with AccessToken, SlackResponse[returnIm.ChannelType]] =
    ZIO.serviceWithZIO[Slack](_.openConversation(channel, returnIm, users))

  /**
   * https://api.slack.com/methods/conversations.rename
   */
  def renameConversation(
    channel: String,
    name: String
  ): URIO[Slack with AccessToken, SlackResponse[RenameConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.renameConversation(channel, name))

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
  ): URIO[Slack with AccessToken, SlackResponse[ResponseChunk[Message]]] =
    ZIO.serviceWithZIO[Slack](_.getConversationReplies(channel, ts, cursor, inclusive, latest, limit, oldest))

  /**
   * https://api.slack.com/methods/conversations.setPurpose
   */
  def setConversationPurpose(
    channel: String,
    purpose: String
  ): URIO[Slack with AccessToken, SlackResponse[SetPurposeConversationsResponse]] =
    ZIO.serviceWithZIO[Slack](_.setConversationPurpose(channel, purpose))

  /**
   * https://api.slack.com/methods/conversations.setTopic
   */
  def setConversationTopic(
    channel: String,
    topic: String
  ): URIO[Slack with AccessToken, SlackResponse[SetTopicConversationsResponse]] =
//    request("conversations.setTopic")
//      .jsonBody(Json.obj("channel" -> channel.asJson, "topic" -> topic.asJson))
//      .at[Conversation]("channel")
    ZIO.serviceWithZIO[Slack](_.setConversationTopic(channel, topic))

  /**
   * https://api.slack.com/methods/conversations.unarchive
   */
  def unarchiveConversation(
    channel: String
  ): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.unarchiveConversation(channel))
}

object Conversations extends GeneratedConversations
