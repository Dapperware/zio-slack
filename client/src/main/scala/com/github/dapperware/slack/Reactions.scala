package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedReactions
import com.github.dapperware.slack.generated.requests.{
  AddReactionsRequest,
  GetReactionsRequest,
  RemoveReactionsRequest
}
import com.github.dapperware.slack.models.{ Plural, ResponseChunk }
import io.circe.Json
import zio.{ Has, URIO }

trait Reactions { self: Slack =>
  def addReactionToMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Reactions.addReactions(AddReactionsRequest(channel = channelId, name = emojiName, timestamp = timestamp)))

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Reactions.removeReactions(RemoveReactionsRequest(emojiName, file, fileComment, channelId, timestamp)))

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Reactions.getReactions(GetReactionsRequest(file, fileComment, channelId, full, timestamp)))

  def getReactionsForMessage(
    channelId: String,
    timestamp: String,
    full: Option[Boolean]
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(
    userId: Option[String] = None,
    full: Option[Boolean] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    teamId: Option[String] = None,
    cursor: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[ResponseChunk[Json]]] = {
    implicit val plural: Plural[Json] = Plural.const("items")

    apiCall(
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
    )
  }

  def removeReactionFromMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    removeReaction(emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}

private[slack] trait ReactionsAccessors { _: Slack.type =>
  def addReactionToMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.addReactionToMessage(emojiName, channelId, timestamp))

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.removeReaction(emojiName, file, fileComment, channelId, timestamp))

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.getReactions(file, fileComment, channelId, timestamp, full))

  def getReactionsForMessage(
    channelId: String,
    timestamp: String,
    full: Option[Boolean]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(
    userId: Option[String] = None,
    full: Option[Boolean] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    teamId: Option[String] = None,
    cursor: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ResponseChunk[Json]]] =
    URIO.accessM(_.get.listReactionsForUser(userId, full, count, page, teamId, cursor))

  def removeReactionFromMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    removeReaction(emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}

object Reactions extends GeneratedReactions
