package com.github.dapperware.slack.api

import com.github.dapperware.slack._
import zio.ZIO

trait SlackApps {
  def uninstall: ZIO[SlackEnv with ClientSecret, SlackError, Boolean] =
    for {
      (id, secret) <- clientId <&> clientSecret
      ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
    } yield ok
}

object apps extends SlackApps
