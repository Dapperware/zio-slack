/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedStars {

  /**
   * Adds a star to an item.
   * @see https://api.slack.com/methods/stars.add
   */
  def addStars(req: AddStarsRequest): Request[Unit, AccessToken] =
    request("stars.add").jsonBody(req).auth.accessToken

  /**
   * Lists stars for a user.
   * @see https://api.slack.com/methods/stars.list
   */
  def listStars(req: ListStarsRequest): Request[ListStarsResponse, AccessToken] =
    request("stars.list").formBody(req).as[ListStarsResponse].auth.accessToken

  /**
   * Removes a star from an item.
   * @see https://api.slack.com/methods/stars.remove
   */
  def removeStars(req: RemoveStarsRequest): Request[Unit, AccessToken] =
    request("stars.remove").jsonBody(req).auth.accessToken

}
