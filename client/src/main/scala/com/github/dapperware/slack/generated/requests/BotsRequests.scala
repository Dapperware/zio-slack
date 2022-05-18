/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param bot Bot user to get info on
 */
case class InfoBotsRequest(bot: Option[String] = None)

object InfoBotsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoBotsRequest] = FormEncoder.fromParams.contramap[InfoBotsRequest] { req =>
    List("bot" -> req.bot)
  }
}
