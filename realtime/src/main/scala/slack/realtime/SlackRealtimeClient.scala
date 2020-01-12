package slack.realtime

import slack._
import slack.api.{ as, request, sendM }
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.asynchttpclient.zio.ZioWebSocketHandler
import sttp.client.ws.WebSocket
import zio.{ Task, UIO, ZIO }

trait SlackRealtimeClient {
  val slackRealtimeClient: SlackRealtimeClient.Service[Any]
}

object SlackRealtimeClient {
  trait Service[R] {
    private[slack] def openWebsocket: ZIO[R with SlackEnv, SlackError, WebSocket[Task]]
  }

  def make(backend: SttpBackend[Task, Nothing, WebSocketHandler]): UIO[SlackRealtimeClient] =
    UIO.effectTotal(new SlackRealtimeClient {
      override val slackRealtimeClient: Service[Any] = new Service[Any] {
        private[slack] override def openWebsocket: ZIO[SlackEnv, SlackError, WebSocket[Task]] =
          for {
            url <- sendM(request("rtm.connect")) >>= as[String]("url")
            r <- ZioWebSocketHandler().flatMap { handler =>
                  backend.openWebsocket(basicRequest.get(uri"$url"), handler)
                }
          } yield r.result
      }
    })
}
