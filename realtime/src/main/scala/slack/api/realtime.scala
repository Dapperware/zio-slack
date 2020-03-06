package slack.api

import slack.SlackError
import slack.realtime.{ Rtm, SlackRealtimeEnv }
import sttp.client.ws.WebSocket
import zio.{ Task, ZIO }

object realtime extends Rtm.Service[SlackRealtimeEnv] {}
