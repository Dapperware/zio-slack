package com.github.dapperware.slack

import com.github.dapperware.slack.SlackException.RatelimitError
import sttp.client3._
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client3.asynchttpclient.zio.stubbing._
import sttp.model.{ Header, StatusCode }
import zio.duration.durationInt
import zio.test.Assertion.{ equalTo, fails }
import zio.test._

object SlackClientLiveSpec extends DefaultRunnableSpec {
  def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("SlackClient")(
    suite("Special case: Rate limiting")(
//      testM(
//        "Retrieves the retry after header"
//      )(for {
//        client   <- SlackClient.make
//        _        <- whenRequestMatchesPartial { case _ =>
//                      Response(
//                        "Hello",
//                        StatusCode.TooManyRequests,
//                        "Rate limited",
//                        List(
//                          Header("Retry-After", 30.toString)
//                        )
//                      )
//                    }
//        response <- client
//                      .send(
//                        basicRequest
//                          .get(uri"https://api.slack.com/")
//                          .response(asString.mapWithMetadata(ResponseAs.deserializeRightCatchingExceptions(identity)))
//                      )
//                      .run
//      } yield assert(response.untraced)(fails(equalTo(RatelimitError(Some(30.seconds))))))
    ).provideCustomLayer(AsyncHttpClientZioBackend.stubLayer.orDie)
  )
}
