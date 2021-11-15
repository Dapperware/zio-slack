package com.github.dapperware.slack

import com.github.dapperware.slack.{ authenticateM, AccessToken }
import sttp.client3.{ basicRequest, _ }
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test._

object AccessTokenSpec extends DefaultRunnableSpec {

  val accessToken = AccessToken.make("abc123")

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("AccessToken")(
    testM("Adds auth bearer token") {
      val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

      assertM(authenticateM(request).map(_.headers))(contains(new Header("Authorization", "Bearer abc123")))
    }.provideLayer(accessToken.toLayer)
  )
}
