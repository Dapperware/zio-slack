package com.github.dapperware.slack

import sttp.client3._
import sttp.model.Header
import zio.ZLayer
import zio.test.Assertion.contains
import zio.test._

object ClientSecretSpec extends ZIOSpecDefault {

  val token = ZLayer(ClientSecret.make("abc123", "supersecret"))

  override def spec =
    suite("ClientSecret")(
      test("Adds auth basic id and secret in base64") {

        val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

        assertZIO(ClientSecret.authenticateM(request).map(_.headers))(
          contains(new Header("Authorization", "Basic YWJjMTIzOnN1cGVyc2VjcmV0"))
        )
      }
    ).provide(token)
}
