package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import zio.ZIO

trait SlackPins {
  def pin(channelId: String, timeStamp: Option[String] = None): ZIO[SlackEnv, SlackError, Boolean] = 
    sendM(request("pins.add", "channel" -> channelId, "timestamp" -> timeStamp)) >>= isOk
}
