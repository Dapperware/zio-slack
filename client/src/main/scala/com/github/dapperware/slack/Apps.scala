package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedApps
import com.github.dapperware.slack.generated.requests.UninstallAppsRequest
import zio.{ Trace, ZIO }

trait Apps { self: SlackApiBase =>
  def uninstall(implicit trace: Trace): ZIO[AccessToken with ClientSecret, Nothing, SlackResponse[Unit]] =
    ZIO
      .serviceWithZIO[ClientSecret](clientSecret =>
        apiCall(
          Apps.uninstallApps(
            UninstallAppsRequest(client_id = clientSecret.clientId, client_secret = clientSecret.clientSecret)
          )
        )
      )

  def openSocketModeConnection(implicit trace: Trace): ZIO[AppToken, Nothing, SlackResponse[String]] =
    apiCall(
      request("apps.connections.open")
        .jsonAt[String]("url")
        .auth
        .appToken
    )
}

private[slack] trait AppsAccessors { self: Slack.type =>
  def uninstall(implicit trace: Trace): ZIO[Slack with AccessToken with ClientSecret, Nothing, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.uninstall)

  def openSocketModeConnection(implicit trace: Trace): ZIO[Slack with AppToken, Nothing, SlackResponse[String]] =
    ZIO.serviceWithZIO[Slack](_.openSocketModeConnection)
}

object Apps extends GeneratedApps
