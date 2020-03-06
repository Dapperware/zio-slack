package slack.api

import slack.models.{ Group, HistoryChunk }
import slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackGroups {

  @deprecated("Please use the conversations API instead", "0.3.4")
  trait Service {

    def archiveGroup(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.archive", "channel" -> channelId)) >>= isOk

    def closeGroup(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.close", "channel" -> channelId)) >>= isOk

    def createGroup(name: String): ZIO[SlackEnv, SlackError, Group] =
      sendM(request("groups.create", "name" -> name)) >>= as[Group]("group")

    def createChildGroup(channelId: String): ZIO[SlackEnv, SlackError, Group] =
      sendM(request("groups.createChild", "channel" -> channelId)) >>= as[Group]("group")

    def getGroupHistory(channelId: String,
                        latest: Option[String] = None,
                        oldest: Option[String] = None,
                        inclusive: Option[Int] = None,
                        count: Option[Int] = None): ZIO[SlackEnv, SlackError, HistoryChunk] =
      sendM(
        request("groups.history",
                "channel"   -> channelId,
                "latest"    -> latest,
                "oldest"    -> oldest,
                "inclusive" -> inclusive,
                "count"     -> count)
      ) >>= as[HistoryChunk]

    def getGroupInfo(channelId: String): ZIO[SlackEnv, SlackError, Group] =
      sendM(request("groups.info", "channel" -> channelId)) >>= as[Group]("group")

    def inviteToGroup(channelId: String, userId: String): ZIO[SlackEnv, SlackError, Group] =
      sendM(request("groups.invite", "channel" -> channelId, "user" -> userId)) >>= as[Group]("group")

    def kickFromGroup(channelId: String, userId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.kick", "channel" -> channelId, "user" -> userId)) >>= isOk

    def leaveGroup(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.leave", "channel" -> channelId)) >>= isOk

    def listGroups(excludeArchived: Int = 0): ZIO[SlackEnv, SlackError, Seq[Group]] =
      sendM(request("groups.list", "exclude_archived" -> excludeArchived.toString)) >>= as[Seq[Group]]("groups")

    def markGroup(channelId: String, ts: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.mark", "channel" -> channelId, "ts" -> ts)) >>= isOk

    def openGroup(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.open", "channel" -> channelId)) >>= isOk

    // TODO: Lite Group Object
    def renameGroup(channelId: String, name: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.rename", "channel" -> channelId, "name" -> name)) >>= isOk

    def setGroupPurpose(channelId: String, purpose: String): ZIO[SlackEnv, SlackError, String] =
      sendM(request("groups.setPurpose", "channel" -> channelId, "purpose" -> purpose)) >>= as[String]("purpose")

    def setGroupTopic(channelId: String, topic: String): ZIO[SlackEnv, SlackError, String] =
      sendM(request("groups.setTopic", "channel" -> channelId, "topic" -> topic)) >>= as[String]("topic")

    def unarchiveGroup(channelId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("groups.unarchive", "channel" -> channelId)) >>= isOk
  }
}

@deprecated("Please use the conversations API instead", "0.3.4")
object groups extends SlackGroups.Service
