package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import zio.ZIO

trait SlackEmojis {
  def listEmojis: ZIO[SlackEnv, SlackError, Map[String, String]] =
    sendM(request("emoji.list")) >>= as[Map[String, String]]("emoji")
}

object emojis extends SlackEmojis
