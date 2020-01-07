package slack.api

import io.circe.Json
import io.circe.syntax._
import slack.models.{ Channel, Conversation, Message }
import slack.{ as, isOk, request, requestJson, sendM, ChannelLike, ChannelLikeId, SlackEnv, SlackError }
import zio.ZIO

trait SlackConversations {
  val slackConversations: SlackConversations.Service[Any]
}

object SlackConversations {
  trait Service[R] {
    def archiveConversation(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("conversations.archive", "channel_id" -> channelId)) >>= isOk

    def closeConversation(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("conversations.close", "channel_id" -> channelId)) >>= isOk
    def createConversation(name: String,
                           isPrivate: Option[Boolean] = None,
                           userIds: Option[List[String]]): ZIO[R with SlackEnv, SlackError, Conversation] =
      sendM(
        request("conversations.create",
                "name"       -> name,
                "is_private" -> isPrivate,
                "user_ids"   -> userIds.map(_.mkString(",")))
      ) >>= as[Conversation]("channel")
//    def getConversationHistory(channelId: String,
//                               cursor: Option[String] = None,
//                               inclusive: Option[Boolean] = None,
//                               latest: Option[String] = None,
//                               limit: Option[Int] = None,
//                               oldest: Option[String] = None): ZIO[R with SlackEnv, SlackError, MessagesChunk]
    def getConversationInfo(channel: String,
                            includeLocale: Option[Boolean] = None,
                            includeNumMembers: Option[Boolean] = None): ZIO[R with SlackEnv, Throwable, Channel] =
      sendM(
        request("conversations.info",
                "channel"             -> channel,
                "include_locale"      -> includeLocale,
                "include_num_members" -> includeNumMembers)
      ) >>= as[Channel]("channel")

    def inviteToConversation(channel: String, users: List[String]): ZIO[R with SlackEnv, Throwable, Channel] =
      sendM(request("conversations.invite", "channel" -> channel, "users" -> users.mkString(","))) >>= as[Channel](
        "channel"
      )
    def joinConversation(channel: String): ZIO[R with SlackEnv, Throwable, Channel] =
      sendM(requestJson("conversations.join", Json.obj("channel" -> channel.asJson))) >>= as[Channel]("channel")
    def kickFromConversation(channel: String, user: String): ZIO[R with SlackEnv, Throwable, Boolean] =
      sendM(requestJson("conversations.kick", Json.obj("channel" -> channel.asJson, "user" -> user.asJson))) >>= isOk
    def leaveConversation(channel: String): ZIO[R with SlackEnv, Throwable, Boolean] =
      sendM(requestJson("conversations.leave", Json.obj("channel" -> channel.asJson))) >>= isOk
    def listConversations(cursor: Option[String] = None,
                          excludeArchived: Option[Boolean] = None,
                          limit: Option[Int] = None,
                          types: Option[List[String]] = None): ZIO[R with SlackEnv, Throwable, List[Channel]] =
      sendM(
        request("conversations.list",
                "cursor"           -> cursor,
                "exclude_archived" -> excludeArchived,
                "limit"            -> limit,
                "types"            -> types.map(_.mkString(",")))
      ) >>= as[List[Channel]]("channels")

    def getConversationMembers(channel: String, cursor: Option[String] = None, limit: Option[Int] = None) =
      sendM(
        request(
          "conversations.members",
          "channel" -> channel,
          "cursor"  -> cursor,
          "limit"   -> limit
        )
      ) >>= as[List[String]]("members")

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

    def renameConversation(channel: String, name: String): ZIO[R with SlackEnv, Throwable, Channel] =
      sendM(request("conversations.rename", "channel" -> channel, "name" -> name)) >>= as[Channel]("channel")

    def getConversationReplies(channel: String,
                               ts: String,
                               cursor: Option[String] = None,
                               inclusive: Option[Boolean] = None,
                               latest: Option[String] = None,
                               limit: Option[Int] = None,
                               oldest: Option[String] = None): ZIO[R with SlackEnv, Throwable, List[Message]] =
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
    def setConversationPurpose(
      channel: String,
      purpose: String
    ): ZIO[R with SlackEnv, Throwable, String] =
      sendM(
        requestJson("conversations.setPurpose", Json.obj("channel" -> channel.asJson, "purpose" -> purpose.asJson))
      ) >>= as[String]("purpose")

    def setConversationTopic(
      channel: String,
      topic: String
    ): ZIO[R with SlackEnv, Throwable, String] =
      sendM(
        requestJson("conversations.setTopic", Json.obj("channel" -> channel.asJson, "topic" -> topic.asJson))
      ) >>= as[String]("topic")

    def unarchiveConversation(
      channel: String
    ) =
      sendM(requestJson("conversations.unarchive", Json.obj("channel" -> channel.asJson))) >>= isOk
  }
}

object conversations extends SlackConversations.Service[SlackEnv]
