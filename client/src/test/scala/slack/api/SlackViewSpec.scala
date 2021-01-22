package slack.api

import com.github.dapperware.slack.models.{
  ConversationSelectElement,
  InputBlock,
  PlainTextInput,
  PlainTextObject,
  View
}
import io.circe.parser
import zio.test.Assertion.{ equalTo, isRight }
import zio.test._

object SlackViewSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("View")(
      test("deserialize modal payload") {
        val payload =
          """
            |{
            |  "type": "modal",
            |  "callback_id": "modal-with-inputs",
            |  "title": {
            |    "type": "plain_text",
            |    "text": "Modal with inputs"
            |  },
            |  "blocks": [
            |    {
            |      "type": "input",
            |      "block_id": "multiline",
            |      "label": {
            |        "type": "plain_text",
            |        "text": "Enter your value"
            |      },
            |      "element": {
            |        "type": "plain_text_input",
            |        "multiline": true,
            |        "action_id": "mlvalue"
            |      }
            |    },
            |    {
            |      "block_id": "target_channel",
            |      "type": "input",
            |      "optional": true,
            |      "label": {
            |        "type": "plain_text",
            |        "text": "Select a channel to post the result on"
            |      },
            |      "element": {
            |        "action_id": "target_select",
            |        "type": "conversations_select",
            |        "response_url_enabled": true,
            |        "placeholder": {"type": "plain_text", "text": "This one"}
            |      }
            |    }
            |  ],
            |  "submit": {
            |    "type": "plain_text",
            |    "text": "Submit"
            |  }
            |}""".stripMargin

        val view = parser.parse(payload).flatMap(_.as[View])

        assert(view)(
          isRight(
            equalTo(
              View(
                "modal",
                PlainTextObject("Modal with inputs"),
                callback_id = Some("modal-with-inputs"),
                blocks = List(
                  InputBlock(
                    PlainTextObject("Enter your value"),
                    PlainTextInput(
                      "mlvalue",
                      multiline = Some(true)
                    ),
                    block_id = Some("multiline"),
                  ),
                  InputBlock(
                    PlainTextObject("Select a channel to post the result on"),
                    ConversationSelectElement(
                      response_url_enabled = Some(true),
                      action_id = "target_select",
                      placeholder = PlainTextObject("This one")
                    ),
                    block_id = Some("target_channel"),
                    optional = Some(true)
                  )
                ),
                submit = Some(PlainTextObject("Submit"))
              )
            )
          )
        )
      }
    )
}