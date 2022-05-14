package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedRequest
import io.circe.Json
import zio.{Has, URIO}

trait Teams {

  def getTeamAccessLogs(count: Option[Int], page: Option[Int]): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Json]] =
    Request.make("team.accessLogs")
      .formBody("count" -> count, "page" -> page)
      .as[Json].toCall

  def getTeamInfo: URIO[Has[Slack] with Has[AccessToken], SlackResponse[Json]] =
    Request.make("team.info").as[Json].toCall
}
