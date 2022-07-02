/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedRtm {

  /**
   * Starts a Real Time Messaging session.
   * @see https://api.slack.com/methods/rtm.connect
   */
  def connectRtm(req: ConnectRtmRequest): Request[ConnectRtmResponse, AccessToken] =
    request("rtm.connect").formBody(req).as[ConnectRtmResponse].auth.accessToken

}
