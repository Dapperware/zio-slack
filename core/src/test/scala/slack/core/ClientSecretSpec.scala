package slack.core

import sttp.client._
import sttp.model.Header
import zio.test.Assertion.contains
import zio.test.{ assert, suite, testM, DefaultRunnableSpec }

object ClientSecretSpec
    extends DefaultRunnableSpec(
      suite("ClientSecret")(
        testM("Adds auth basic id and secret in base64") {
          for {
            accessToken <- ClientSecret.make("abc123", "supersecret")
            request     = basicRequest.get(uri"https://github.com/dapperware/zio-slack")
            newRequest  <- ClientSecret.authenticateM(request) provide accessToken
          } yield assert(newRequest.headers, contains(new Header("Authorization", "Basic YWJjMTIzOnN1cGVyc2VjcmV0")))
        }
      )
    )
