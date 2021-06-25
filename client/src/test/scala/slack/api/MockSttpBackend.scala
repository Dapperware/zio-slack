package slack.api

import com.github.dapperware.slack.AccessToken
import zio.Layer
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClientStubbing
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
trait MockSttpBackend {

  def sttbBackEndStubLayer: Layer[Nothing, SttpClient with SttpClientStubbing] = AsyncHttpClientZioBackend.stubLayer.mapError(_ => ???)

  def accessTokenLayer(accessToken: String): Layer[Nothing, AccessToken] = AccessToken.make(accessToken).toLayer

}
