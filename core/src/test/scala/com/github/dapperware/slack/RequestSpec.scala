package com.github.dapperware.slack

import sttp.client3.Response
import sttp.client3.asynchttpclient.zio.{ stubbing, SttpClientStubbing }
import sttp.model.Header
import sttp.model.StatusCode.TooManyRequests
import zio.duration.durationInt
import zio.magic._
import zio.test._

object RequestSpec extends DefaultRunnableSpec {

  def whenAnyRequest: SttpClientStubbing.StubbingWhenRequest = stubbing.whenAnyRequest

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("Request")(
    testM("ratelimit handling") {
      for {
        _        <- whenAnyRequest.thenRespond(Response("", TooManyRequests, "", headers = List(Header("Retry-After", "100"))))
        request   = Request.make(MethodName("test"))
        response <- SlackClient.apiCall(request)
      } yield assertTrue(response == SlackError.RatelimitError(100.seconds))
    }
  ).injectCustom(
    HttpSlack.layer,
    SttpClientStubbing.layer
  )
}
