package slack

import slack.models.{Reaction, ReactionsResponse}
import zio.ZIO

//@mockable
//@accessible
trait SlackReactions {
  val slackReactions: SlackReactions.Service[Any]
}

object SlackReactions {

  trait Service[R] {
    def addReaction(emojiName: String,
                    file: Option[String],
                    fileComment: Option[String],
                    channelId: Option[String],
                    timestamp: Option[String]): ZIO[R with SlackEnv, SlackError, Boolean] = ???

    def addReactionToMessage(emojiName: String,
                             channelId: String,
                             timestamp: String): ZIO[R with SlackEnv, SlackError, Boolean] = ???

    def removeReaction(
      emojiName: String,
      file: Option[String] = None,
      fileComment: Option[String] = None,
      channelId: Option[String] = None,
      timestamp: Option[String] = None
    ): ZIO[R with SlackEnv, SlackError, Boolean] = ???

    def getReactions(file: Option[String] = None,
                     fileComment: Option[String] = None,
                     channelId: Option[String] = None,
                     timestamp: Option[String] = None,
                     full: Option[Boolean]): ZIO[R with SlackEnv, SlackError, Seq[Reaction]] =
      sendM(
        request("reactions.get",
                "file" -> file,
                "file_comment" -> fileComment,
                "channel" -> channelId,
                "timestamp" -> timestamp,
                "full" -> full)
      ) >>= as[Seq[Reaction]]

    def getReactionsForMessage(channelId: String,
                               timestamp: String,
                               full: Option[Boolean]): ZIO[R with SlackEnv, SlackError, Seq[Reaction]] =
      getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

    def listReactionsForUser(userId: Option[String],
                             full: Boolean,
                             count: Option[Int],
                             page: Option[Int]): ZIO[R with SlackEnv, SlackError, ReactionsResponse] =
      sendM(request("reactions.list", "user" -> userId, "full" -> full, "count" -> count, "page" -> page)) >>= as[
        ReactionsResponse
      ]

    def removeReactionFromMessage(emojiName: String,
                                  channelId: String,
                                  timestamp: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      removeReaction(emojiName = emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
  }
}

object reactions extends SlackReactions.Service[SlackEnv]
