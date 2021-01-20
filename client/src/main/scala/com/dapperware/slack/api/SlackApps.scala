package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import com.dapperware.slack.access.ClientSecret
import zio.ZIO

trait SlackApps {
  def uninstall: ZIO[SlackEnv with ClientSecret, SlackError, Boolean] =
    for {
      (id, secret) <- ClientSecret.clientId <&> ClientSecret.clientSecret
      ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
    } yield ok
}

object apps extends SlackApps
