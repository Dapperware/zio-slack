package slack.api.models

import com.github.dapperware.slack.models.{ InputBlockElement, PlainTextInput, Plural, ResponseChunk, ResponseMetadata }
import io.circe.parser
import io.circe.syntax.EncoderOps
import zio.Chunk
import zio.random.Random
import zio.test.Assertion._
import zio.test._
import com.github.dapperware.slack.models.PlainTextObject
import com.github.dapperware.slack.models.DispatchActionConfig
import com.github.dapperware.slack.models.TriggerAction

object SerializationSpec extends DefaultRunnableSpec {

  val plainTextObjectGen: Gen[Sized with Random, PlainTextObject] = 
    for {
      text <- Gen.alphaNumericString
      emoji <- Gen.option(Gen.boolean)
      typ <- Gen.const("plain_text")
    } yield PlainTextObject(text, emoji, typ)


  val dispatchActionConfigGen: Gen[Sized with Random, DispatchActionConfig] = 
    for {
      triggerActionsOn <- Gen.listOf(Gen.oneOf(Gen.const(TriggerAction.OnEnterPressed), Gen.const(TriggerAction.OnCharacterEntered)))
    } yield DispatchActionConfig(triggerActionsOn)

  implicit val blockInputGen: Gen[Sized with Random, PlainTextInput] =
    for {
      actionId <- Gen.alphaNumericString
      placeholder <- Gen.option(plainTextObjectGen)
      initialValue <- Gen.option(Gen.alphaNumericString)
      multiline <- Gen.option(Gen.boolean)
      minLenght_ <- Gen.int(0, Int.MaxValue - 1)
      minLength <- Gen.option(Gen.const(minLenght_))
      maxLength <- Gen.option(Gen.int(minLenght_, Int.MaxValue))
      dispatchActionConfig <- Gen.option(dispatchActionConfigGen)
    } yield PlainTextInput(actionId, placeholder, initialValue, multiline, minLength, minLength, dispatchActionConfig)

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
