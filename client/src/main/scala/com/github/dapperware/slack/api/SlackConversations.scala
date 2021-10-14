package com.github.dapperware.slack.api

import com.github.dapperware.slack.models._
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import io.circe.Json
import io.circe.syntax._
import zio.ZIO

trait SlackConversations {

  /**
   * https://api.slack.com/methods/conversations.archive
   */
  def archiveConversation(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("conversations.archive", "channel_id" -> channelId)) >>= isOk

  /**
   * https://api.slack.com/methods/conversations.close
   */
  def closeConversation(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("conversations.close", "channel_id" -> channelId)) >>= isOk

  /**
   * https://api.slack.com/methods/conversations.create
   */
  def createConversation(name: String,
                         isPrivate: Option[Boolean] = None,
                         userIds: Option[List[String]]): ZIO[SlackEnv, SlackError, Conversation] =
    sendM(
      request("conversations.create",
              "name"       -> name,
              "is_private" -> isPrivate,
              "user_ids"   -> userIds.map(_.mkString(",")))
    ) >>= as[Conversation]("channel")

  /**
   * https://api.slack.com/methods/conversations.history
   */
  def getConversationHistory(channelId: String,
                             cursor: Option[String] = None,
                             inclusive: Option[Boolean] = None,
                             latest: Option[String] = None,
                             limit: Option[Int] = None,
                             oldest: Option[String] = None): ZIO[SlackEnv, SlackError, HistoryChunk] =
    sendM(
      request("conversations.history",
              "channel"   -> channelId,
              "cursor"    -> cursor,
              "inclusive" -> inclusive,
              "latest"    -> latest,
              "limit"     -> limit,
              "oldest"    -> oldest)
    ) >>= as[HistoryChunk]

  def getSingleMessage(channelId: String, ts: String): ZIO[SlackEnv, SlackError, Option[HistoryItem]] =
    getConversationHistory(channelId, latest = Some(ts), inclusive = Some(true), limit = Some(1))
      .map(_.messages.headOption)

  /**
   * https://api.slack.com/methods/conversations.info
   */
  def getConversationInfo(channel: String,
                          includeLocale: Option[Boolean] = None,
                          includeNumMembers: Option[Boolean] = None): ZIO[SlackEnv, Throwable, Channel] =
    sendM(
      request("conversations.info",
              "channel"             -> channel,
              "include_locale"      -> includeLocale,
              "include_num_members" -> includeNumMembers)
    ) >>= as[Channel]("channel")

  /**
   * https://api.slack.com/methods/conversations.invite
   */
  def inviteToConversation(channel: String, users: List[String]): ZIO[SlackEnv, Throwable, Channel] =
    sendM(request("conversations.invite", "channel" -> channel, "users" -> users.mkString(","))) >>= as[Channel](
      "channel"
    )

  /**
   * https://api.slack.com/methods/conversations.join
   */
  def joinConversation(channel: String): ZIO[SlackEnv, Throwable, Channel] =
    sendM(requestJson("conversations.join", Json.obj("channel" -> channel.asJson))) >>= as[Channel]("channel")

  /**
   * https://api.slack.com/methods/conversations.kick
   */
  def kickFromConversation(channel: String, user: String): ZIO[SlackEnv, Throwable, Boolean] =
    sendM(requestJson("conversations.kick", Json.obj("channel" -> channel.asJson, "user" -> user.asJson))) >>= isOk

  /**
   * https://api.slack.com/methods/conversations.leave
   */
  def leaveConversation(channel: String): ZIO[SlackEnv, Throwable, Boolean] =
    sendM(requestJson("conversations.leave", Json.obj("channel" -> channel.asJson))) >>= isOk

  /**
   * https://api.slack.com/methods/conversations.list
   */
  def listConversations(cursor: Option[String] = None,
                        excludeArchived: Option[Boolean] = None,
                        limit: Option[Int] = None,
                        types: Option[List[String]] = None): ZIO[SlackEnv, Throwable, ResponseChunk[Channel]] =
    sendM(
      request("conversations.list",
              "cursor"           -> cursor,
              "exclude_archived" -> excludeArchived,
              "limit"            -> limit,
              "types"            -> types.map(_.mkString(",")))
    ) >>= as[ResponseChunk[Channel]]

  /**
   * https://api.slack.com/methods/conversations.members
   */
  def getConversationMembers(channel: String,
                             cursor: Option[String] = None,
                             limit: Option[Int] = None): ZIO[SlackEnv, Throwable, ResponseChunk[String]] = {
    implicit val plural: Plural[String] = Plural.const("members")

    sendM(
      request(
        "conversations.members",
        "channel" -> channel,
        "cursor"  -> cursor,
        "limit"   -> limit
      )
    ) >>= as[ResponseChunk[String]]
  }

  /**
   * https://api.slack.com/methods/conversations.open
   */
  def openConversation[T](channel: Option[String] = None,
                          returnIm: ChannelLike[T] = ChannelLikeId,
                          users: Option[List[String]] = None): ZIO[SlackEnv, Throwable, returnIm.ChannelType] =
    sendM(
      requestJson(
        "conversations.open",
        Json.obj(
          "channel"   -> channel.asJson,
          "return_im" -> returnIm.isFull.asJson,
          "users"     -> users.map(_.mkString(",")).asJson
        )
      )
    ) >>= (returnIm.extract(_, "channel"))

  /**
   * https://api.slack.com/methods/conversations.rename
   */
  def renameConversation(channel: String, name: String): ZIO[SlackEnv, Throwable, Channel] =
    sendM(request("conversations.rename", "channel" -> channel, "name" -> name)) >>= as[Channel]("channel")

  /**
   * https://api.slack.com/methods/conversations.replies
   */
  def getConversationReplies(channel: String,
                             ts: String,
                             cursor: Option[String] = None,
                             inclusive: Option[Boolean] = None,
                             latest: Option[String] = None,
                             limit: Option[Int] = None,
                             oldest: Option[String] = None): ZIO[SlackEnv, Throwable, List[Message]] =
    sendM(
      request("conversations.replies",
              "channel"   -> channel,
              "ts"        -> ts,
              "cursor"    -> cursor,
              "inclusive" -> inclusive,
              "latest"    -> latest,
              "limit"     -> limit,
              "oldest"    -> oldest)
    ) >>= as[List[Message]]("messages")

  /**
   * https://api.slack.com/methods/conversations.setPurpose
   */
  def setConversationPurpose(
    channel: String,
    purpose: String
  ): ZIO[SlackEnv, Throwable, String] =
    sendM(
      requestJson("conversations.setPurpose", Json.obj("channel" -> channel.asJson, "purpose" -> purpose.asJson))
    ) >>= as[String]("purpose")

  /**
   * https://api.slack.com/methods/conversations.setTopic
   */
  def setConversationTopic(
    channel: String,
    topic: String
  ): ZIO[SlackEnv, Throwable, String] =
    sendM(
      requestJson("conversations.setTopic", Json.obj("channel" -> channel.asJson, "topic" -> topic.asJson))
    ) >>= as[String]("topic")

  /**
   * https://api.slack.com/methods/conversations.unarchive
   */
  def unarchiveConversation(
    channel: String
  ) =
    sendM(requestJson("conversations.unarchive", Json.obj("channel" -> channel.asJson))) >>= isOk
}

object conversations extends SlackConversations
