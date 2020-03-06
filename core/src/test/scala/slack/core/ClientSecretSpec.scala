package slack.core

import slack.core.access.{ secret, AccessToken }
import sttp.client._
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test.{ assertM, suite, testM, DefaultRunnableSpec, ZSpec }

object ClientSecretSpec extends DefaultRunnableSpec {

  val token = AccessToken.liveSecret("abc123", "supersecret")

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("ClientSecret")(
      testM("Adds auth basic id and secret in base64") {

        val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

        assertM(secret.authenticateM(request).map(_.headers))(
          contains(new Header("Authorization", "Basic YWJjMTIzOnN1cGVyc2VjcmV0"))
        )
      }
    ).provideLayer(token)
}
