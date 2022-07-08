package com.github.dapperware.slack

import sttp.client3.Response
import sttp.client3.asynchttpclient.zio.{ stubbing, SttpClientStubbing }
import sttp.model.Header
import sttp.model.StatusCode.TooManyRequests
import zio.durationInt
import zio.test._

object RequestSpec extends ZIOSpecDefault {

  def whenAnyRequest: SttpClientStubbing.StubbingWhenRequest = stubbing.whenAnyRequest

  override def spec = suite("Request")(
    test("ratelimit handling") {
      for {
        _        <- whenAnyRequest.thenRespond(Response("", TooManyRequests, "", headers = List(Header("Retry-After", "100"))))
        request   = Request.make(MethodName("test"))
        response <- SlackClient.apiCall(request)
      } yield assertTrue(response == SlackError.RatelimitError(100.seconds))
    }
  ).provide(
    HttpSlack.layer,
    SttpClientStubbing.layer
  )
}
