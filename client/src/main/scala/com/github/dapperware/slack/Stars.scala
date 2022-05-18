package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedAuthRequest
import com.github.dapperware.slack.generated.GeneratedStars
import com.github.dapperware.slack.generated.requests.{ AddStarsRequest, ListStarsRequest, RemoveStarsRequest }
import com.github.dapperware.slack.generated.responses.ListStarsResponse
import zio.{ Has, URIO }

trait Stars {
  def addStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Stars.addStars(AddStarsRequest(channel, file, file_comment = fileComment, timestamp = timestamp)).toCall

  def listStars(
    user: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListStarsResponse]] =
    Stars.listStars(ListStarsRequest(user, count.map(_.toString), page.map(_.toString))).toCall

  def removeStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Stars.removeStars(RemoveStarsRequest(channel, file, file_comment = fileComment, timestamp = timestamp)).toCall

}

object Stars extends GeneratedStars
