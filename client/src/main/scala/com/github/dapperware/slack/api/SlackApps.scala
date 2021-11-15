package com.github.dapperware.slack.api

import com.github.dapperware.slack._
import zio.{ Has, ZIO }

trait SlackApps {
  def uninstall: ZIO[SlackEnv with Has[ClientSecret], SlackError, Boolean] =
    for {
      (id, secret) <- ClientSecret.clientId <&> ClientSecret.clientSecret
      ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
    } yield ok
}

object apps extends SlackApps
