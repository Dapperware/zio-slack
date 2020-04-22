package slack.core

import sttp.client.basicRequest
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test.{ assertM, suite, testM, DefaultRunnableSpec, ZSpec }
import sttp.client._

object AccessTokenSpec extends DefaultRunnableSpec {

  val accessToken = AccessToken.make("abc123")

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("AccessToken")(
    testM("Adds auth bearer token") {
      val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

      assertM(access.authenticateM(request).map(_.headers))(contains(new Header("Authorization", "Bearer abc123")))
    }.provideLayer(accessToken.toLayer)
  )
}
