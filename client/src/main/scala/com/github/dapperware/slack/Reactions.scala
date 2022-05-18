package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.generated.GeneratedReactions
import com.github.dapperware.slack.generated.requests.{
  AddReactionsRequest,
  GetReactionsRequest,
  RemoveReactionsRequest
}
import com.github.dapperware.slack.models.{ Plural, ResponseChunk }
import io.circe.Json

trait Reactions {
  def addReactionToMessage(emojiName: String, channelId: String, timestamp: String) =
    Reactions.addReactions(AddReactionsRequest(emojiName, channelId, timestamp)).toCall

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  ) =
    Reactions.removeReactions(RemoveReactionsRequest(emojiName, file, fileComment, channelId, timestamp)).toCall

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  ) =
    Reactions.getReactions(GetReactionsRequest(file, fileComment, channelId, full, timestamp)).toCall

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

object Reactions extends GeneratedReactions
