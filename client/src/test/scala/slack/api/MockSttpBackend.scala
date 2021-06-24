package slack.api

import com.github.dapperware.slack.AccessToken
import sttp.client3.testing.SttpBackendStub
import zio.Task
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import zio.{ Layer, ZLayer }
import sttp.client3.asynchttpclient.zio.SttpClient

trait MockSttpBackend {
  def sttpBackEndStub: SttpBackendStub[Task, ZioStreams with WebSockets] =
    AsyncHttpClientZioBackend.stub

  def accessTokenLayer(accessToken: String) = AccessToken.make(accessToken).toLayer

  def sttpClientLayer: Layer[Nothing, SttpClient] = ZLayer.fromFunction((_: Any) => sttpBackEndStub)
}
