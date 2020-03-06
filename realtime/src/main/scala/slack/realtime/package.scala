package slack

import slack.api.{ as, request, sendM }
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.asynchttpclient.zio.ZioWebSocketHandler
import sttp.client.ws.WebSocket
import zio.{ Has, Task, ZIO, ZLayer }

package object realtime {
  type SlackRealtimeClient = Has[SlackRealtimeClient.Service]
  type SlackRealtimeEnv    = SlackEnv with SlackRealtimeClient

  object SlackRealtimeClient {
    trait Service {
      private[slack] def openWebsocket: ZIO[SlackEnv, SlackError, WebSocket[Task]]
    }

    def live(backend: SttpBackend[Task, Nothing, WebSocketHandler]): ZLayer.NoDeps[Nothing, SlackRealtimeClient] =
      ZLayer.succeed(new Service {
        private[slack] override def openWebsocket: ZIO[SlackEnv, SlackError, WebSocket[Task]] =
          for {
            url <- sendM(request("rtm.connect")) >>= as[String]("url")
            r <- ZioWebSocketHandler().flatMap { handler =>
                  backend.openWebsocket(basicRequest.get(uri"$url"), handler)
                }
          } yield r.result
      })
  }

  private[slack] def openWebsocket: ZIO[SlackEnv with SlackRealtimeClient, SlackError, WebSocket[Task]] =
    ZIO.accessM[SlackRealtimeClient with SlackEnv](_.get.openWebsocket)
}
