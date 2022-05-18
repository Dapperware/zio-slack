/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedDialog {

  /**
   * Open a dialog with a user
   * @see https://api.slack.com/methods/dialog.open
   */
  def openDialog(req: OpenDialogRequest): Request[Unit, AccessToken] =
    request("dialog.open").jsonBody(req).auth.accessToken

}
