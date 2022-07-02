package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedDialog
import com.github.dapperware.slack.generated.requests.OpenDialogRequest
import com.github.dapperware.slack.models.Dialog
import io.circe.syntax._
import zio.{ Has, URIO }

trait Dialogs { self: Slack =>

  def openDialog(triggerId: String, dialog: Dialog) =
    apiCall(Dialogs.openDialog(OpenDialogRequest(trigger_id = triggerId, dialog = dialog.asJson.noSpaces)))

}

private[slack] trait DialogsAccessors { _: Slack.type =>

  def openDialog(triggerId: String, dialog: Dialog): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.openDialog(triggerId, dialog))

}

object Dialogs extends GeneratedDialog
