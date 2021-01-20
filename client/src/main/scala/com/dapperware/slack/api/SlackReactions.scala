package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import com.dapperware.slack.models.{Reaction, ReactionsResponse}
import io.circe.Json
import io.circe.syntax._
import zio.ZIO

trait SlackReactions {

  def addReactionToMessage(emojiName: String,
                           channelId: String,
                           timestamp: String): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(
      requestJson("reactions.add",
                  Json.obj(
                    "name"      -> emojiName.asJson,
                    "channel"   -> channelId.asJson,
                    "timestamp" -> timestamp.asJson
                  ))
    ) >>= isOk

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  ): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(
      requestJson(
        "reactions.remove",
        Json.obj(
          "name"         -> emojiName.asJson,
          "file"         -> file.asJson,
          "file_comment" -> fileComment.asJson,
          "timestamp"    -> timestamp.asJson,
          "channel"      -> channelId.asJson
        )
      )
    ) >>= isOk

  def getReactions(file: Option[String] = None,
                   fileComment: Option[String] = None,
                   channelId: Option[String] = None,
                   timestamp: Option[String] = None,
                   full: Option[Boolean]): ZIO[SlackEnv, SlackError, Seq[Reaction]] =
    sendM(
      request("reactions.get",
              "file"         -> file,
              "file_comment" -> fileComment,
              "channel"      -> channelId,
              "timestamp"    -> timestamp,
              "full"         -> full)
    ) >>= as[Seq[Reaction]]

  def getReactionsForMessage(channelId: String,
                             timestamp: String,
                             full: Option[Boolean]): ZIO[SlackEnv, SlackError, Seq[Reaction]] =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(userId: Option[String],
                           full: Boolean,
                           count: Option[Int],
                           page: Option[Int]): ZIO[SlackEnv, SlackError, ReactionsResponse] =
    sendM(request("reactions.list", "user" -> userId, "full" -> full, "count" -> count, "page" -> page)) >>= as[
      ReactionsResponse
    ]

  def removeReactionFromMessage(emojiName: String,
                                channelId: String,
                                timestamp: String): ZIO[SlackEnv, SlackError, Boolean] =
    removeReaction(emojiName = emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}

object reactions extends SlackReactions
