package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedReactions
import com.github.dapperware.slack.generated.requests.{AddReactionsRequest, GetReactionsRequest, RemoveReactionsRequest}
import com.github.dapperware.slack.models.{Plural, ResponseChunk}
import io.circe.Json
import zio.{Trace, URIO, ZIO}

trait Reactions { self: SlackApiBase =>
  def addReactionToMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Reactions.addReactions(AddReactionsRequest(channel = channelId, name = emojiName, timestamp = timestamp)))

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Reactions.removeReactions(RemoveReactionsRequest(emojiName, file, fileComment, channelId, timestamp)))

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Reactions.getReactions(GetReactionsRequest(file, fileComment, channelId, full, timestamp)))

  def getReactionsForMessage(
    channelId: String,
    timestamp: String,
    full: Option[Boolean]
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(
    userId: Option[String] = None,
    full: Option[Boolean] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    teamId: Option[String] = None,
    cursor: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[ResponseChunk[Json]]] = {
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
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    removeReaction(emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}

private[slack] trait ReactionsAccessors { self: Slack.type =>
  def addReactionToMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.addReactionToMessage(emojiName, channelId, timestamp))

  def removeReaction(
    emojiName: String,
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.removeReaction(emojiName, file, fileComment, channelId, timestamp))

  def getReactions(
    file: Option[String] = None,
    fileComment: Option[String] = None,
    channelId: Option[String] = None,
    timestamp: Option[String] = None,
    full: Option[Boolean]
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.getReactions(file, fileComment, channelId, timestamp, full))

  def getReactionsForMessage(
    channelId: String,
    timestamp: String,
    full: Option[Boolean]
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    getReactions(channelId = Some(channelId), timestamp = Some(timestamp), full = full)

  def listReactionsForUser(
    userId: Option[String] = None,
    full: Option[Boolean] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    teamId: Option[String] = None,
    cursor: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[ResponseChunk[Json]]] =
    ZIO.serviceWithZIO[Slack](_.listReactionsForUser(userId, full, count, page, teamId, cursor))

  def removeReactionFromMessage(
    emojiName: String,
    channelId: String,
    timestamp: String
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    removeReaction(emojiName, channelId = Some(channelId), timestamp = Some(timestamp))
}

object Reactions extends GeneratedReactions
