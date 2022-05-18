package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedAuthRequest
import com.github.dapperware.slack.generated.GeneratedApps
import com.github.dapperware.slack.generated.requests.UninstallAppsRequest
import zio.{ Has, ZIO }

trait Apps {
  def uninstall: ZIO[Has[Slack] with Has[AccessToken] with Has[ClientSecret], Nothing, SlackResponse[Unit]] =
    ClientSecret.get.flatMap { clientSecret =>
      Apps.uninstallApps(UninstallAppsRequest(Some(clientSecret.clientId), Some(clientSecret.clientId))).toCall
    }
}

object Apps extends GeneratedApps
