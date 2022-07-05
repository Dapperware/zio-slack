/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedViews {

  /**
   * Open a view for a user.
   * @see https://api.slack.com/methods/views.open
   */
  def openViews(req: OpenViewsRequest): Request[OpenViewsResponse, AccessToken] =
    request("views.open").jsonBody(req).as[OpenViewsResponse].auth.accessToken

  /**
   * Publish a static view for a User.
   * @see https://api.slack.com/methods/views.publish
   */
  def publishViews(req: PublishViewsRequest): Request[PublishViewsResponse, AccessToken] =
    request("views.publish").jsonBody(req).as[PublishViewsResponse].auth.accessToken

  /**
   * Push a view onto the stack of a root view.
   * @see https://api.slack.com/methods/views.push
   */
  def pushViews(req: PushViewsRequest): Request[PushViewsResponse, AccessToken] =
    request("views.push").jsonBody(req).as[PushViewsResponse].auth.accessToken

  /**
   * Update an existing view.
   * @see https://api.slack.com/methods/views.update
   */
  def updateViews(req: UpdateViewsRequest): Request[UpdateViewsResponse, AccessToken] =
    request("views.update").jsonBody(req).as[UpdateViewsResponse].auth.accessToken

}
