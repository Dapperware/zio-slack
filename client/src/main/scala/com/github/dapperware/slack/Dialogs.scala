package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedDialog
import com.github.dapperware.slack.generated.requests.OpenDialogRequest
import com.github.dapperware.slack.models.Dialog
import io.circe.syntax._
import zio.{ URIO, ZIO }

trait Dialogs { self: Slack =>

  def openDialog(triggerId: String, dialog: Dialog) =
    apiCall(Dialogs.openDialog(OpenDialogRequest(trigger_id = triggerId, dialog = dialog.asJson.noSpaces)))

}

private[slack] trait DialogsAccessors { _: Slack.type =>

  def openDialog(triggerId: String, dialog: Dialog): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.openDialog(triggerId, dialog))

}

object Dialogs extends GeneratedDialog
