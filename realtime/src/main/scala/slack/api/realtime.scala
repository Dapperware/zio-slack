package slack.api

import slack.SlackError
import slack.realtime.{ Rtm, SlackRealtimeClient, SlackRealtimeEnv }
import sttp.client.ws.WebSocket
import zio.{ Task, ZIO }

object realtime extends Rtm.Service[SlackRealtimeEnv] {

  private[slack] def openWebsocket: ZIO[SlackRealtimeEnv, SlackError, WebSocket[Task]] =
    ZIO.accessM(_.slackRealtimeClient.openWebsocket)

}
