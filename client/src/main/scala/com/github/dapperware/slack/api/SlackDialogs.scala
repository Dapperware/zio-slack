package com.github.dapperware.slack.api

import com.github.dapperware.slack.models.Dialog
import com.github.dapperware.slack.{ SlackEnv, SlackError }
import com.github.dapperware.slack.models.Dialog
import zio.ZIO

trait SlackDialogs {

  def openDialog(triggerId: String, dialog: Dialog): ZIO[SlackEnv, SlackError, Boolean] =
    sendM(request("dialog.open", "trigger_id" -> triggerId, "dialog" -> dialog)) >>= isOk

}

object dialogs extends SlackDialogs
