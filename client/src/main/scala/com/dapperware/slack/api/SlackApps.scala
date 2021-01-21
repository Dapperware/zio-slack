package com.dapperware.slack.api

import com.dapperware.slack.access.{ClientSecret, clientId, clientSecret}
import com.dapperware.slack.{SlackEnv, SlackError}
import zio.ZIO

trait SlackApps {
  def uninstall: ZIO[SlackEnv with ClientSecret, SlackError, Boolean] =
    for {
      (id, secret) <- clientId <&> clientSecret
      ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
    } yield ok
}

object apps extends SlackApps
