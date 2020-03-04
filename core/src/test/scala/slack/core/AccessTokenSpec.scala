package slack.core

import sttp.client.basicRequest
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test.{ assert, suite, testM, DefaultRunnableSpec }
import sttp.client._

object AccessTokenSpec
    extends DefaultRunnableSpec(
      suite("AccessToken")(
        testM("Adds auth bearer token") {
          for {
            accessToken <- AccessToken.make("abc123")
            request     = basicRequest.get(uri"https://github.com/dapperware/zio-slack")
            newRequest  <- AccessToken.authenticateM(request) provide accessToken
          } yield assert(newRequest.headers)(contains(new Header("Authorization", "Bearer abc123")))
        }
      )
    )
