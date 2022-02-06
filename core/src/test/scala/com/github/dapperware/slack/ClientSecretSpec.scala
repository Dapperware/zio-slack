package com.github.dapperware.slack

import com.github.dapperware.slack.ClientSecret
import sttp.client3._
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test._

object ClientSecretSpec extends DefaultRunnableSpec {

  val token = ClientSecret.make("abc123", "supersecret").toLayer

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("ClientSecret")(
      testM("Adds auth basic id and secret in base64") {

        val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

        assertM(ClientSecret.authenticateM(request).map(_.headers))(
          contains(new Header("Authorization", "Basic YWJjMTIzOnN1cGVyc2VjcmV0"))
        )
      }
    ).provideLayer(token)
}
