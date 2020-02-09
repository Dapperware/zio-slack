package slack.api

import io.circe.syntax._
import slack.models.Dialog
import slack.{ SlackEnv, SlackError }
import zio.ZIO

//@accessible
//@mockable
trait SlackDialogs {
  val slackDialogs: SlackDialogs.Service
}

object SlackDialogs {
  trait Service {

    def openDialog(triggerId: String, dialog: Dialog): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("dialog.open", "trigger_id" -> triggerId, "dialog" -> dialog.asJson)) >>= isOk

  }
}

object dialogs extends SlackDialogs.Service
