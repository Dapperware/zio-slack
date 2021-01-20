package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import io.circe.Json
import zio.ZIO

trait SlackStars {
  def addStars(channel: Option[String] = None,
               file: Option[String] = None,
               fileComment: Option[String] = None,
               timestamp: Option[String] = None): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(
      request("stars.add",
              "channel"      -> channel,
              "file"         -> file,
              "file_comment" -> fileComment,
              "timestamp"    -> timestamp)
    ) >>= isOk

  def listStars(userId: Option[String] = None,
                count: Option[Int] = None,
                page: Option[Int] = None): ZIO[SlackEnv, SlackError, Json] =
    sendM(request("stars.list", "user" -> userId, "count" -> count, "page" -> page))

  def removeStars(channel: Option[String] = None,
                  file: Option[String] = None,
                  fileComment: Option[String] = None,
                  timestamp: Option[String] = None): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(
      request("stars.remove",
              "channel"      -> channel,
              "file"         -> file,
              "file_comment" -> fileComment,
              "timestamp"    -> timestamp)
    ) >>= isOk
}

object stars extends SlackStars
