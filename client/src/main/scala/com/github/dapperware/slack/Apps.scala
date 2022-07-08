package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedApps
import com.github.dapperware.slack.generated.requests.UninstallAppsRequest
import zio.ZIO

trait Apps { self: Slack =>
  def uninstall: ZIO[AccessToken with ClientSecret, Nothing, SlackResponse[Unit]] =
    ZIO
      .service[ClientSecret]
      .flatMap(clientSecret =>
        apiCall(
          Apps.uninstallApps(
            UninstallAppsRequest(client_id = clientSecret.clientId, client_secret = clientSecret.clientSecret)
          )
        )
      )

  def openSocketModeConnection: ZIO[AppToken, Nothing, SlackResponse[String]] =
    apiCall(
      request("apps.connections.open")
        .at[String]("url")
        .auth
        .appToken
    )
}

private[slack] trait AppsAccessors { self: Slack.type =>
  def uninstall: ZIO[Slack with AccessToken with ClientSecret, Nothing, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.uninstall)

  def openSocketModeConnection: ZIO[Slack with AppToken, Nothing, SlackResponse[String]] =
    ZIO.serviceWithZIO[Slack](_.openSocketModeConnection)
}

object Apps extends GeneratedApps
