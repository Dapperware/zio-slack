package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedDialog
import com.github.dapperware.slack.generated.requests.OpenDialogRequest
import com.github.dapperware.slack.models.Dialog

trait Dialogs {

  def openDialog(triggerId: String, dialog: Dialog) =
    Dialogs.openDialog(OpenDialogRequest(triggerId, dialog))

}

object Dialogs extends GeneratedDialog
