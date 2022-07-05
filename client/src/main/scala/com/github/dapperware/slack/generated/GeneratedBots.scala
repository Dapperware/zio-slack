/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedBots {

  /**
   * Gets information about a bot user.
   * @see https://api.slack.com/methods/bots.info
   */
  def infoBots(req: InfoBotsRequest): Request[InfoBotsResponse, AccessToken] =
    request("bots.info").formBody(req).as[InfoBotsResponse].auth.accessToken

}
