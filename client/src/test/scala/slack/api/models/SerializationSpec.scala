package slack.api.models

import com.github.dapperware.slack.models.{ InputBlockElement, PlainTextInput, Plural, ResponseChunk, ResponseMetadata }
import io.circe.parser
import io.circe.syntax.EncoderOps
import zio.Chunk
import zio.random.Random
import zio.test.Assertion._
import zio.test.magnolia.DeriveGen
import zio.test._

object SerializationSpec extends DefaultRunnableSpec {

  implicit val blockInputGen: Gen[Sized with Random, PlainTextInput] = {
    implicit val alphaCharGen: DeriveGen[String] = DeriveGen.instance(Gen.string(Gen.alphaNumericChar))
    DeriveGen[PlainTextInput]
  }

  override def spec = suite("Serialization")(
    suite("PlainTextInput")(
      testM("reads what it writes")(check(blockInputGen) { blockInput =>
        val encoded = (blockInput: InputBlockElement).asJson
        val decoded = encoded.as[InputBlockElement]
        assert(decoded)(isRight(equalTo(blockInput))) && assert(encoded.hcursor.downField("type").as[String])(
          isRight(equalTo("plain_text_input"))
        )
      })
    ),
    suite("ResponseChunk")(
      test("can parse") {
        implicit val plural: Plural[String] = Plural.const("members")

        val json =
          """
            |{
            |  "members": ["A", "B"],
            |  "has_more": true,
            |  "response_metadata": {"next_cursor": "C"}
            |}""".stripMargin

        assert(parser.parse(json).flatMap(_.as[ResponseChunk[String]]))(
          isRight(
            equalTo(
              ResponseChunk[String](Chunk("A", "B"), Some(true), Some(ResponseMetadata(Some("C"))))
            )
          )
        )
      }
    )
  )
}
