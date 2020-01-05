package slack.realtime

import slack.SlackError
import sttp.client.ws.WebSocket
import zio.{Task, ZIO}

trait RealtimeApi extends SlackRealtimeClient.Service[SlackRealtimeClient] with Rtm.Service[SlackRealtimeEnv] {

  override private[slack] val openWebsocket: ZIO[SlackRealtimeEnv, SlackError, WebSocket[Task]] =
    ZIO.accessM(_.slackRealtimeClient.openWebsocket)

}

object api extends RealtimeApi
