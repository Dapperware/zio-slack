package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import zio.{ Has, ZIO }

trait Apps {
  def uninstall: ZIO[Has[Slack] with Has[AccessToken] with Has[ClientSecret], Nothing, SlackResponse[Unit]] =
    ClientSecret.get.flatMap { clientSecret =>
      request("apps.uninstall")
        .formBody(
          "client_id"     -> clientSecret.clientId,
          "client_secret" -> clientSecret.clientSecret
        )
        .toCall
    }
}
