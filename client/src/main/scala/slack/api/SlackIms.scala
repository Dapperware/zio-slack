package slack.api

import slack.models.{ HistoryChunk, Im }
import slack.{ SlackEnv, SlackError }
import zio.ZIO
import zio.macros.annotation.mockable

//@accessible
@mockable
@deprecated("Please use conversations api instead", "0.3.4")
trait SlackIms {
  val slackIms: SlackIms.Service[Any]
}

object SlackIms {
  trait Service[R] {
    def closeIm(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("im.close", "channel" -> channelId)) >>= isOk

    def getImHistory(channelId: String,
                     latest: Option[String] = None,
                     oldest: Option[String] = None,
                     inclusive: Option[Int] = None,
                     count: Option[Int] = None): ZIO[R with SlackEnv, SlackError, HistoryChunk] =
      sendM(
        request(
          "im.history",
          "channel"   -> channelId,
          "latest"    -> latest,
          "oldest"    -> oldest,
          "inclusive" -> inclusive,
          "count"     -> count
        )
      ).flatMap(as[HistoryChunk])

    def listIms: ZIO[R with SlackEnv, SlackError, Seq[Im]] =
      sendM(request("im.list")).flatMap(as[Seq[Im]]("ims"))

    def markIm(channelId: String, ts: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("im.mark", "channel" -> channelId, "ts" -> ts)).flatMap(isOk)

    def openIm(userId: String): ZIO[R with SlackEnv, SlackError, String] =
      for {
        res <- sendM(request("im.open", "user" -> userId))
        id  <- ZIO.fromEither(res.hcursor.downField("channel").downField("id").as[String])
      } yield id

  }
}

object ims extends SlackIms.Service[SlackEnv]
