package slack

import zio.test._
import Assertion._
import com.github.dapperware.slack.{ SlackException, SlackExtractors }
import io.circe.CursorOp.DownField
import io.circe.{ DecodingFailure, Json }
import io.circe.syntax._

object ExtractorSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("Extractors")(
    testM("isOk is ok") {
      val json = Json.obj("ok" -> true.asJson)
      assertM(SlackExtractors.isOk(json))(isTrue)
    },
    testM("return response error if not ok") {
      val json = Json.obj("ok" -> false.asJson, "error" -> "not_ok".asJson)
      assertM(SlackExtractors.as[Boolean]("body")(json).flip)(equalTo(SlackException.ResponseError("not_ok")))
    },
    testM("fetch from a nested object") {
      val json = Json.obj("ok" -> true.asJson, "channel" -> "aChannelId".asJson)
      assertM(SlackExtractors.as[String]("channel")(json))(equalTo("aChannelId"))
    },
    testM("fails if ok is not present") {
      val json = Json.obj("channel" -> "aChannelId".asJson)
      assertM(SlackExtractors.as[String]("channel")(json).flip)(
        equalTo(DecodingFailure("Attempt to decode value on failed cursor", List(DownField("ok"))))
      )
    }
  )
}
