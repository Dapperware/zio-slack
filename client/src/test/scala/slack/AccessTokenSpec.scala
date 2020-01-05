package slack

import zio.test._
import sttp.client._
import Assertion._
import sttp.model.Header

object AccessTokenSpec
    extends DefaultRunnableSpec(
      suite("AccessToken")(
        testM("Adds auth bearer token") {
          for {
            accessToken <- AccessToken.make("abc123")
            request     = basicRequest.get(uri"https://github.com/dapperware/zio-slack")
            newRequest  <- AccessToken.authenticateM(request) provide accessToken
          } yield assert(newRequest.headers, contains(new Header("Authorization", "Bearer abc123")))
        }
      )
    )
