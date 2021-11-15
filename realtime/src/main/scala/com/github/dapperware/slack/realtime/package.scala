package com.github.dapperware.slack

import com.github.dapperware.slack.realtime.models.SlackEvent
import zio.Has
import zio.stream.ZStream

package object realtime {
  type MessageStream    = ZStream[Any, SlackError, SlackEvent]
  type SlackRealtimeEnv = SlackEnv with Has[SlackRealtimeClient]
}
