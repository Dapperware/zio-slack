package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedDialog
import com.github.dapperware.slack.generated.requests.OpenDialogRequest
import com.github.dapperware.slack.models.Dialog
import io.circe.syntax._

trait Dialogs {

  def openDialog(triggerId: String, dialog: Dialog) =
    Dialogs.openDialog(OpenDialogRequest(trigger_id = triggerId, dialog = dialog.asJson.noSpaces))

}

object Dialogs extends GeneratedDialog
