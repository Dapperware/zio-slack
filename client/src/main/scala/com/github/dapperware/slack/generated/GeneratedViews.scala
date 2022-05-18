/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedViews {

  /**
   * Open a view for a user.
   * @see https://api.slack.com/methods/views.open
   */
  def openViews(req: OpenViewsRequest): Request[Unit, AccessToken] =
    request("views.open").jsonBody(req).auth.accessToken

  /**
   * Publish a static view for a User.
   * @see https://api.slack.com/methods/views.publish
   */
  def publishViews(req: PublishViewsRequest): Request[Unit, AccessToken] =
    request("views.publish").jsonBody(req).auth.accessToken

  /**
   * Push a view onto the stack of a root view.
   * @see https://api.slack.com/methods/views.push
   */
  def pushViews(req: PushViewsRequest): Request[Unit, AccessToken] =
    request("views.push").jsonBody(req).auth.accessToken

  /**
   * Update an existing view.
   * @see https://api.slack.com/methods/views.update
   */
  def updateViews(req: UpdateViewsRequest): Request[Unit, AccessToken] =
    request("views.update").jsonBody(req).auth.accessToken

}
