package slack.api

import slack.SlackError
import slack.realtime.{ Rtm, SlackRealtimeClient, SlackRealtimeEnv }
import sttp.client.ws.WebSocket
import zio.{ Task, ZIO }

object realtime extends SlackRealtimeClient.Service[SlackRealtimeClient] with Rtm.Service[SlackRealtimeEnv] {

  private[slack] override def openWebsocket: ZIO[SlackRealtimeEnv, SlackError, WebSocket[Task]] =
    ZIO.accessM(_.slackRealtimeClient.openWebsocket)

}
