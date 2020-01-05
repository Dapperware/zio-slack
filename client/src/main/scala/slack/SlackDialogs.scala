package slack

import io.circe.syntax._
import slack.models.Dialog
import zio.ZIO

//@accessible
//@mockable
trait SlackDialogs {
  val slackDialogs: SlackDialogs.Service[Any]
}

object SlackDialogs {
  trait Service[R] {

    def openDialog(triggerId: String, dialog: Dialog): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("dialog.open", "trigger_id" -> triggerId, "dialog" -> dialog.asJson)) >>= isOk

  }
}

object dialogs extends SlackDialogs.Service[SlackEnv]
