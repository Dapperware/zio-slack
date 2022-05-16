package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.models.StarResponse
import zio.{ Has, URIO }

trait Stars {
  def addStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("stars.add")
      .formBody(
        "channel"      -> channel,
        "file"         -> file,
        "file_comment" -> fileComment,
        "timestamp"    -> timestamp
      )
      .toCall

  def listStars(
    user: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[StarResponse]] =
    request("stars.list")
      .formBody(
        "user"  -> user,
        "count" -> count,
        "page"  -> page
      )
      .as[StarResponse]
      .toCall

  def removeStars(
    channel: Option[String] = None,
    file: Option[String],
    fileComment: Option[String] = None,
    timestamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    request("stars.remove")
      .formBody(
        "channel"      -> channel,
        "file"         -> file,
        "file_comment" -> fileComment,
        "timestamp"    -> timestamp
      )
      .toCall

}
