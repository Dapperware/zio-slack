package slack.api

import com.github.dapperware.slack.{ HttpSlack, Slack, SlackResponse }
import sttp.client3.Request
import sttp.client3.asynchttpclient.zio.{ stubbing, SttpClient, SttpClientStubbing }
import sttp.model.Method
import zio.test.{ Assertion, _ }
import zio.{ Has, Layer }

object SlackPinsSpec extends DefaultRunnableSpec with MockSttpBackend {
  private def whenRequestMatches(p: Request[_, _] => Boolean): SttpClientStubbing.StubbingWhenRequest =
    stubbing.whenRequestMatches(p)

  def isOk[A]: Assertion[SlackResponse[A]] =
    Assertion.hasField("ok", _.isOk, Assertion.isTrue)

  private val response      = """
                    {
                    "ok": true
                    }
                """
  private val expectedBody2 = "channel=zoo-channel&timestamp=1234567890.123456"

  private val expectedBody1 = "channel=foo-channel"

  private val stubLayer: Layer[Nothing, SttpClient with Has[SttpClientStubbing.Service]] = sttpBackEndStubLayer

  override def spec: ZSpec[Environment, Failure] = suite("Pins")(
    testM("sends channel-id") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization") == Some("Bearer foo-access-token") && req.body.show.contains(expectedBody1)
      ).thenRespond(response)

      assertM(stubEffect *> Slack.pin("foo-channel"))(isOk)
    },
    testM("sends channel-id and timestamp") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization") == Some("Bearer foo-access-token") && req.body.show.contains(expectedBody2)
      ).thenRespond(response)

      assertM(stubEffect *> Slack.pin("zoo-channel", Some("1234567890.123456")))(isOk)
    }
  ).provideLayer((stubLayer >>> HttpSlack.layer) ++ accessTokenLayer("foo-access-token") ++ stubLayer)
}
