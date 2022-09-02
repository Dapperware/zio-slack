package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedDialog
import com.github.dapperware.slack.generated.requests.OpenDialogRequest
import com.github.dapperware.slack.models.{ dialogFmt, Dialog }
import io.circe.syntax._
import zio.{ Trace, URIO, ZIO }

trait Dialogs { self: SlackApiBase =>

  def openDialog(triggerId: String, dialog: Dialog)(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Dialogs.openDialog(OpenDialogRequest(trigger_id = triggerId, dialog = dialog.asJson.noSpaces)))

}

private[slack] trait DialogsAccessors { self: Slack.type =>

  def openDialog(triggerId: String, dialog: Dialog)(implicit
    trace: Trace
  ): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.openDialog(triggerId, dialog))

}

object Dialogs extends GeneratedDialog
