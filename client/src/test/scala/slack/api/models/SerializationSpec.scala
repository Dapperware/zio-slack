package slack.api.models

import com.github.dapperware.slack.models.{ InputBlockElement, PlainTextInput }
import io.circe.syntax.EncoderOps
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
    )
  )
}
