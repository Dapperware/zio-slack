package slack

import slack.models.{ Channel, HistoryChunk, RepliesChunk }
import zio.ZIO

trait SlackChannels {
  val slackChannels: SlackChannels.Service[Any]
}

object SlackChannels {

  trait Service[R] {
    def archiveChannel(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.archive", "channel" -> channelId)) >>= isOk

    def createChannel(name: String): ZIO[R with SlackEnv, SlackError, Channel] =
      sendM(request("channels.create", "name" -> name)) >>= as[Channel]("channel")

    def getChannelHistory(
      channelId: String,
      latest: Option[String],
      oldest: Option[String],
      inclusive: Option[String],
      count: Option[Int]
    ): ZIO[R with SlackEnv, SlackError, HistoryChunk] =
      sendM(
        request("channels.history",
                "channel"   -> channelId,
                "latest"    -> latest,
                "oldest"    -> oldest,
                "inclusive" -> inclusive,
                "count"     -> count)
      ) >>= as[HistoryChunk]

    def getChannelInfo(channelId: String): ZIO[R with SlackEnv, SlackError, Channel] =
      sendM(request("channels.info", "channel" -> channelId)) >>= as[Channel]("channel")

    def inviteToChannel(channelId: String, userId: String): ZIO[R with SlackEnv, SlackError, Channel] =
      sendM(request("channels.invite", "channel" -> channelId, "user" -> userId)) >>= as[Channel]("channel")

    def joinChannel(channelId: String): ZIO[R with SlackEnv, SlackError, Channel] =
      sendM(request("channels.join", "channel" -> channelId)) >>= as[Channel]("channel")

    def kickFromChannel(channelId: String, userId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.kick", "channel" -> channelId, "user" -> userId)) >>= isOk

    def listChannels(excludeArchived: Int): ZIO[R with SlackEnv, SlackError, Seq[Channel]] =
      sendM(request("channels.list", "exclude_archived" -> excludeArchived)) >>= as[List[Channel]]("channel")

    def leaveChannel(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.leave", "channel" -> channelId)) >>= isOk

    def markChannel(channelId: String, ts: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.mark", "channel" -> channelId, "ts" -> ts)) >>= isOk

    def renameChannel(channelId: String, name: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.rename", "channel" -> channelId, "name" -> name)) >>= isOk

    def getChannelReplies(channelId: String, thread_ts: String): ZIO[R with SlackEnv, SlackError, RepliesChunk] =
      sendM(request("channels.replies", "channel" -> channelId, "thread_ts" -> thread_ts)) >>= as[RepliesChunk]

    def setChannelPurpose(channelId: String, purpose: String): ZIO[R with SlackEnv, SlackError, String] =
      sendM(request("channels.setPurpose", "channel" -> channelId, "purpose" -> purpose)) >>= as[String]("purpose")

    def setChannelTopic(channelId: String, topic: String): ZIO[R with SlackEnv, SlackError, String] =
      sendM(request("channels.setTopic", "channel" -> channelId, "topic" -> topic)) >>= as[String]("topic")

    def unarchiveChannel(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("channels.unarchive", "channel" -> channelId)) >>= isOk
  }
}

object channels extends SlackChannels.Service[SlackEnv]
