package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request

trait Pins {
  def pin(channelId: String, timeStamp: Option[String] = None) =
    request("pins.add").formBody("channel" -> channelId, "timestamp" -> timeStamp)
}
