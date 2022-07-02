package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedApps
import zio.{ Has, ZIO }

trait Apps {
  def uninstall: ZIO[Has[Slack] with Has[AccessToken] with Has[ClientSecret], Nothing, SlackResponse[Unit]] =
    Apps.uninstallApps.toCall
}

object Apps extends GeneratedApps
