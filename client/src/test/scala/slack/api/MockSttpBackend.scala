package slack.api

import com.github.dapperware.slack.AccessToken
import zio.Layer
import sttp.client3.httpclient.zio.SttpClient
import sttp.client3.httpclient.zio.SttpClientStubbing
import sttp.client3.httpclient.zio.HttpClientZioBackend


trait MockSttpBackend {

  def sttbBackEndStubLayer: Layer[Nothing, SttpClient with SttpClientStubbing] = HttpClientZioBackend.stubLayer

  def accessTokenLayer(accessToken: String) = AccessToken.make(accessToken).toLayer

}
