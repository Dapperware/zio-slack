package slack.api

import com.github.dapperware.slack.AccessToken
import sttp.client3.asynchttpclient.zio.{ AsyncHttpClientZioBackend, SttpClient, SttpClientStubbing }
import zio.{ Layer, ZLayer }
trait MockSttpBackend {

  val sttpBackEndStubLayer: Layer[Nothing, SttpClient with SttpClientStubbing] =
    AsyncHttpClientZioBackend.stubLayer.orDie

  def accessTokenLayer(accessToken: String): Layer[Nothing, AccessToken] = ZLayer(AccessToken.make(accessToken))

}
