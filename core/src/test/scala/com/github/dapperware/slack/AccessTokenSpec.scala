package com.github.dapperware.slack

import sttp.client3._
import sttp.model.Header
import zio.ZLayer
import zio.test.Assertion.contains
import zio.test._

object AccessTokenSpec extends ZIOSpecDefault {

  val accessToken = AccessToken.make("abc123")

  override def spec = suite("AccessToken")(
    test("Adds auth bearer token") {
      val request = basicRequest.get(uri"https://github.com/dapperware/zio-slack")

      assertZIO(authenticateZIO(request).map(_.headers))(contains(new Header("Authorization", "Bearer abc123")))
    }.provide(ZLayer(accessToken))
  )
}
