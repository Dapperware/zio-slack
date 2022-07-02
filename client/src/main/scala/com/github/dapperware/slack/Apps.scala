package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedApps
import com.github.dapperware.slack.generated.requests.UninstallAppsRequest
import zio.{ Has, ZIO }

trait Apps { self: Slack =>
  def uninstall: ZIO[Has[AccessToken] with Has[ClientSecret], Nothing, SlackResponse[Unit]] =
    ZIO
      .service[ClientSecret]
      .flatMap(clientSecret =>
        apiCall(
          Apps.uninstallApps(
            UninstallAppsRequest(client_id = clientSecret.clientId, client_secret = clientSecret.clientSecret)
          )
        )
      )
}

private[slack] trait AppsAccessors { self: Slack.type =>
  def uninstall: ZIO[Has[Slack] with Has[AccessToken] with Has[ClientSecret], Nothing, SlackResponse[Unit]] =
    ZIO.accessM(_.get.uninstall)
}

object Apps extends GeneratedApps
