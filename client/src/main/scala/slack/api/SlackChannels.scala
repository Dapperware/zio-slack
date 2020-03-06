package slack.api

import slack._
import slack.models.{ Channel, HistoryChunk, RepliesChunk }
import zio.ZIO

object SlackChannels {

  @deprecated("Please use the conversations API instead", "0.3.4")
  trait Service {
    def archiveChannel(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.archive", "channel" -> channelId)) >>= isOk

    def createChannel(name: String): ZIO[SlackEnv, SlackError, Channel] =
      sendM(request("channels.create", "name" -> name)) >>= as[Channel]("channel")

    def getChannelHistory(
      channelId: String,
      latest: Option[String],
      oldest: Option[String],
      inclusive: Option[String],
      count: Option[Int]
    ): ZIO[SlackEnv, SlackError, HistoryChunk] =
      sendM(
        request("channels.history",
                "channel"   -> channelId,
                "latest"    -> latest,
                "oldest"    -> oldest,
                "inclusive" -> inclusive,
                "count"     -> count)
      ) >>= as[HistoryChunk]

    def getChannelInfo(channelId: String): ZIO[SlackEnv, SlackError, Channel] =
      sendM(request("channels.info", "channel" -> channelId)) >>= as[Channel]("channel")

    def inviteToChannel(channelId: String, userId: String): ZIO[SlackEnv, SlackError, Channel] =
      sendM(request("channels.invite", "channel" -> channelId, "user" -> userId)) >>= as[Channel]("channel")

    def joinChannel(channelId: String): ZIO[SlackEnv, SlackError, Channel] =
      sendM(request("channels.join", "channel" -> channelId)) >>= as[Channel]("channel")

    def kickFromChannel(channelId: String, userId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.kick", "channel" -> channelId, "user" -> userId)) >>= isOk

    def listChannels(excludeArchived: Int): ZIO[SlackEnv, SlackError, Seq[Channel]] =
      sendM(request("channels.list", "exclude_archived" -> excludeArchived)) >>= as[List[Channel]]("channel")

    def leaveChannel(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.leave", "channel" -> channelId)) >>= isOk

    def markChannel(channelId: String, ts: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.mark", "channel" -> channelId, "ts" -> ts)) >>= isOk

    def renameChannel(channelId: String, name: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.rename", "channel" -> channelId, "name" -> name)) >>= isOk

    def getChannelReplies(channelId: String, thread_ts: String): ZIO[SlackEnv, SlackError, RepliesChunk] =
      sendM(request("channels.replies", "channel" -> channelId, "thread_ts" -> thread_ts)) >>= as[RepliesChunk]

    def setChannelPurpose(channelId: String, purpose: String): ZIO[SlackEnv, SlackError, String] =
      sendM(request("channels.setPurpose", "channel" -> channelId, "purpose" -> purpose)) >>= as[String]("purpose")

    def setChannelTopic(channelId: String, topic: String): ZIO[SlackEnv, SlackError, String] =
      sendM(request("channels.setTopic", "channel" -> channelId, "topic" -> topic)) >>= as[String]("topic")

    def unarchiveChannel(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("channels.unarchive", "channel" -> channelId)) >>= isOk
  }
}

@deprecated("Please use the conversations API instead", "0.3.4")
object channels extends SlackChannels.Service
