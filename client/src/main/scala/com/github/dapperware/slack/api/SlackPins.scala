package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import zio.ZIO


trait SlackPins {
  def pin(channelId: String, timeStamp: Option[String] = None): ZIO[SlackEnv, SlackError, Boolean] = {
    val channelHeader = "channel" -> channelId
    val headers = timeStamp.fold(Seq(channelHeader))(ts => Seq(channelHeader, "timestamp" -> ts))
    sendM(requestQueryParams("pins.add", headers)) >>= isOk
  }
}
