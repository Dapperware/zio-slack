package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedStars
import com.github.dapperware.slack.generated.requests.{ AddStarsRequest, ListStarsRequest, RemoveStarsRequest }
import com.github.dapperware.slack.generated.responses.ListStarsResponse
import zio.{ URIO, ZIO }

trait Stars { self: Slack =>
  def addStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Stars.addStars(AddStarsRequest(channel, file, file_comment = fileComment, timestamp = timestamp)))

  def listStars(
    cursor: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    limit: Option[Int] = None
  ): URIO[AccessToken, SlackResponse[ListStarsResponse]] =
    apiCall(
      Stars
        .listStars(
          ListStarsRequest(count = count.map(_.toString), page = page.map(_.toString), limit = limit, cursor = cursor)
        )
    )

  def removeStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Stars.removeStars(RemoveStarsRequest(channel, file, file_comment = fileComment, timestamp = timestamp)))

}

private[slack] trait StarsAccessors { self: Slack.type =>
  def addStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.addStars(channel, file, fileComment, timestamp))

  def listStars(
    cursor: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None,
    limit: Option[Int] = None
  ): URIO[Slack with AccessToken, SlackResponse[ListStarsResponse]] =
    ZIO.serviceWithZIO[Slack](_.listStars(cursor, count, page, limit))

  def removeStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.removeStars(channel, file, fileComment, timestamp))

}

object Stars extends GeneratedStars
