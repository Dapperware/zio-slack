package slack.api

import com.github.dapperware.slack.{ MockSttpBackend, Slack, SlackResponse }
import com.github.dapperware.slack.SttpStubbing.whenRequestMatches
import sttp.model.Method
import zio.test._

object SlackPinsSpec extends ZIOSpecDefault with MockSttpBackend {
  def isOk[A]: Assertion[SlackResponse[A]] =
    Assertion.hasField("ok", _.isOk, Assertion.isTrue)

  private val response      = SlackResponse.ok(())
  private val expectedBody2 = """{"channel":"zoo-channel","timestamp":"1234567890.123456"}"""

  private val expectedBody1 = """{"channel":"foo-channel"}"""

  override def spec = suite("Pins")(
    test("sends channel-id") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization").contains("Bearer foo-access-token") && req.body.show.contains(expectedBody1)
      ).thenRespond(response)

      assertZIO(stubEffect *> Slack.pin("foo-channel"))(isOk)
    },
    test("sends channel-id and timestamp") {
      val stubEffect = whenRequestMatches(req =>
        req.uri.toString == "https://slack.com/api/pins.add" && req.method == Method.POST &&
          req.header("Authorization").contains("Bearer foo-access-token") && req.body.show.contains(expectedBody2)
      ).thenRespond(response)

      assertZIO(stubEffect *> Slack.pin("zoo-channel", Some("1234567890.123456")))(isOk)
    }
  ).provide(sttpBackEndStubLayer, Slack.http, accessTokenLayer("foo-access-token"))
}
