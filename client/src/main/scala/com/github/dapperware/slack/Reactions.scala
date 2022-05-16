package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.{ Plural, Reaction, ReactionsResponse, ResponseChunk }
import io.circe.Json
import io.circe.syntax._

trait Reactions {
  def addReactionToMessage(emojiName: String, channelId: String, timestamp: String) =
    request("reactions.add").jsonBody(
      Json.obj("name" -> emojiName.asJson, "channel" -> channelId.asJson, "timestamp" -> timestamp.asJson)
    )

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  ) =
    request("reactions.remove").jsonBody(
      Json.obj(
        "name"         -> emojiName.asJson,
        "file"         -> file.asJson,
        "file_comment" -> fileComment.asJson,
        "channel"      -> channelId.asJson,
        "timestamp"    -> timestamp.asJson
      )
    )

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  ) =
    request("reactions.get")
      .formBody(
        "file"         -> file,
        "file_comment" -> fileComment,
        "channel"      -> channelId,
        "timestamp"    -> timestamp,
        "full"         -> full
      )
      .as[List[Reaction]]

  def getReactionsForMessage(channelId: String, timestamp: String, full: Option[Boolean]) =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(
    userId: Option[String] = None,
    full: Option[Boolean] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    teamId: Option[String] = None,
    cursor: Option[String] = None
  ) = {
    implicit val plural: Plural[Json] = Plural.const("items")

    request("reactions.list")
      .formBody(
        "user"    -> userId,
        "full"    -> full,
        "count"   -> count,
        "page"    -> page,
        "team_id" -> teamId,
        "cursor"  -> cursor
      )
      .as[ResponseChunk[Json]]
  }

  def removeReactionFromMessage(emojiName: String, channelId: String, timestamp: String) =
    removeReaction(emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}
