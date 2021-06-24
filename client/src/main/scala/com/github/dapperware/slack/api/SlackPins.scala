package com.github.dapperware.slack.api

import com.github.dapperware.slack.SlackParamMagnet
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import zio.ZIO

trait SlackPins {
  def pin(channelId: String, timeStamp: Option[String] = None): ZIO[SlackEnv, SlackError, Boolean] = {
    val channel: (String, SlackParamMagnet) = "channel" -> channelId
    timeStamp match {
      case Some(value) => sendM(request("pins.add", channel, "timestamp" -> value)) >>= isOk
      case None => sendM(request("pins.add", channel)) >>= isOk
    }
  }
}
