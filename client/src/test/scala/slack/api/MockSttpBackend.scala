package slack.api

import com.github.dapperware.slack.AccessToken
import zio.{ Has, Layer }
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClientStubbing
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
trait MockSttpBackend {

  def sttpBackEndStubLayer: Layer[Nothing, SttpClient with SttpClientStubbing] =
    AsyncHttpClientZioBackend.stubLayer.orDie

  def accessTokenLayer(accessToken: String): Layer[Nothing, Has[AccessToken]] = AccessToken.make(accessToken).toLayer

}
