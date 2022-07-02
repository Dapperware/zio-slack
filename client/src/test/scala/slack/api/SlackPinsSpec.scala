package slack.api

import com.github.dapperware.slack.SlackClient
import zio.test._
import zio.test.Assertion
import sttp.model.Method
import sttp.client3.asynchttpclient.zio.stubbing._
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.asynchttpclient.zio.SttpClientStubbing
import zio.{ Has, Layer }

object SlackPinsSpec extends DefaultRunnableSpec with MockSttpBackend {

  private val response      = """
                    {
                    "ok": true
                    }
                """
  private val expectedBody2 = "channel=zoo-channel&timestamp=1234567890.123456"

  private val expectedBody1 = "channel=foo-channel"

  private val stubLayer: Layer[Nothing, SttpClient with Has[SttpClientStubbing.Service]] = sttbBackEndStubLayer

  override def spec: ZSpec[Environment, Failure] = suite("Pins")(
    testM("sends channel-id") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization") == Some("Bearer foo-access-token") && req.body.show.contains(expectedBody1)
      ).thenRespond(response)

      assertM(stubEffect *> web.pin("foo-channel"))(
        Assertion.isTrue
      )
    },
    testM("sends channel-id and timestamp") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization") == Some("Bearer foo-access-token") && req.body.show.contains(expectedBody2)
      ).thenRespond(response)

      assertM(stubEffect *> web.pin("zoo-channel", Some("1234567890.123456")))(
        Assertion.isTrue
      )
    }
  ).provideLayer((stubLayer >>> SlackClient.live) ++ accessTokenLayer("foo-access-token") ++ stubLayer)
}
