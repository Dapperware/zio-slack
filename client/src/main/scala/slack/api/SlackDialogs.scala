package slack.api

import slack.models.Dialog
import slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackDialogs {
  trait Service {

    def openDialog(triggerId: String, dialog: Dialog): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("dialog.open", "trigger_id" -> triggerId, "dialog" -> dialog)) >>= isOk

  }
}

object dialogs extends SlackDialogs.Service
