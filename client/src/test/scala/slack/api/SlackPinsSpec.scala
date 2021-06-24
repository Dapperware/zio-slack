package slack.api

import com.github.dapperware.slack.api.web
import com.github.dapperware.slack.client.SlackClient
import zio.test._
import sttp.client3.testing.SttpBackendStub
import zio.Task
import zio.test.Assertion
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.model.Method

object SlackPinsSpec extends DefaultRunnableSpec with MockSttpBackend {

    private val response = """
                    {
                    "ok": true
                    }
                """
    override def sttpBackEndStub: SttpBackendStub[Task, ZioStreams with WebSockets] = 
        super.sttpBackEndStub
            .whenRequestMatches{request => 
                (request.uri.toString == "https://slack.com/api/pins.add?channel=foo-channel" || 
                    request.uri.toString == "https://slack.com/api/pins.add?channel=zoo-channel&timestamp=1234567890.123456") && 
                    request.method == Method.POST &&
                    request.header("Authorization") == Some("Bearer foo-access-token")

            }
            .thenRespond(response)

    override def spec: ZSpec[Environment,Failure] = suite("Pins")(
        testM("sends channel-id") {
            assertM(web.pin("foo-channel"))(
                Assertion.isTrue
            )
        },
        testM("sends channel-id and timestamp") {
            assertM(web.pin("zoo-channel", Some("1234567890.123456")))(
                Assertion.isTrue
            )
        }
    ).provideLayer((sttpClientLayer >>> SlackClient.live) ++ accessTokenLayer("foo-access-token"))

  
}
