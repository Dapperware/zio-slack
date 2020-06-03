package slack.api

import slack.{ ClientSecret, SlackError }
import zio.{ Has, ZIO }

trait SlackApps {
  def uninstall: ZIO[slack.SlackEnv with Has[ClientSecret], SlackError, Boolean] =
    for {
      (id, secret) <- ClientSecret.clientId <&> ClientSecret.clientSecret
      ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
    } yield ok
}

object apps extends SlackApps
