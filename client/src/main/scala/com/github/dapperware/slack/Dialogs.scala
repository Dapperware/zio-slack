package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.Dialog

trait Dialogs {

  def openDialog(triggerId: String, dialog: Dialog) =
    request("dialog.open").formBody("trigger_id" -> triggerId, "dialog" -> dialog)

}
