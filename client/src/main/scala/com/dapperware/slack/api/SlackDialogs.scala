package com.dapperware.slack.api

import com.dapperware.slack.models.Dialog
import com.dapperware.slack.{SlackEnv, SlackError}
import zio.ZIO

trait SlackDialogs {

  def openDialog(triggerId: String, dialog: Dialog): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("dialog.open", "trigger_id" -> triggerId, "dialog" -> dialog)) >>= isOk

}

object dialogs extends SlackDialogs
