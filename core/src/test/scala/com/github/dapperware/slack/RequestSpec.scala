package com.github.dapperware.slack

import com.github.dapperware.slack.SttpStubbing.whenAnyRequest
import sttp.client3.Response
import sttp.model.Header
import sttp.model.StatusCode.TooManyRequests
import zio.{ durationInt, ZLayer }
import zio.test._

object RequestSpec extends ZIOSpecDefault {

  override def spec = suite("Request")(
    test("ratelimit handling") {
      for {
        _        <- whenAnyRequest.thenRespond(Response("", TooManyRequests, "", headers = List(Header("Retry-After", "100"))))
        request   = Request.make(MethodName("test")).auth.none
        response <- SlackClient.apiCall(request)
      } yield assertTrue(response == SlackError.RatelimitError(100.seconds))
    }
  ).provide(
    HttpSlack.layer,
    SttpStubbing.layer,
    ZLayer.succeed(NoAuth)
  )
}
