package slack

import zio.test._
import Assertion._
import com.github.dapperware.slack.SlackResponse.Ok
import com.github.dapperware.slack.{ SlackError, SlackResponse }
import io.circe.CursorOp.DownField
import io.circe.{ DecodingFailure, Json }
import io.circe.syntax._

object ExtractorSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] = suite("Extractors")(
    test("isOk is ok") {
      val json = Json.obj("ok" -> true.asJson)
      assert(json.as[SlackResponse[Unit]].map(_.isOk))(isRight(isTrue))
    },
    test("return response error if not ok") {
      val json = Json.obj("ok" -> false.asJson, "error" -> "not_ok".asJson)
      assert(json.as[SlackResponse[Unit]])(isRight(equalTo(SlackError.ApiError("not_ok"))))
    },
    test("fetch from a nested object") {
      val json    = Json.obj("ok" -> true.asJson, "channel" -> "aChannelId".asJson)
      val decoder = SlackResponse.decodeWith[String](_.hcursor.get[String]("channel"))
      assert(decoder.decodeJson(json))(isRight(equalTo(Ok("aChannelId", Nil))))
    },
    test("fails if ok is not present") {
      val json    = Json.obj("channel" -> "aChannelId".asJson)
      val decoder = SlackResponse.decoder[String](_.get[String]("channel"))
      assert(decoder.decodeJson(json).left.map(_.getMessage()))(
        isLeft(equalTo(DecodingFailure("Missing required field", List(DownField("ok"))).getMessage()))
      )
    },
    test("warnings don't cause an error") {
      val json = Json.obj("ok" -> true.asJson, "warning" -> List("aWarning").asJson)
      assert(json.as[SlackResponse[Unit]].map(_.toEitherWith((_, w) => w)))(isRight(isRight(equalTo(List("aWarning")))))
    }
  )
}
