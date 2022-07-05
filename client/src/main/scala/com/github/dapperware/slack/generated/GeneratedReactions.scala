/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedReactions {

  /**
   * Adds a reaction to an item.
   * @see https://api.slack.com/methods/reactions.add
   */
  def addReactions(req: AddReactionsRequest): Request[Unit, AccessToken] =
    request("reactions.add").jsonBody(req).auth.accessToken

  /**
   * Gets reactions for an item.
   * @see https://api.slack.com/methods/reactions.get
   */
  def getReactions(req: GetReactionsRequest): Request[Unit, AccessToken] =
    request("reactions.get").formBody(req).auth.accessToken

  /**
   * Lists reactions made by a user.
   * @see https://api.slack.com/methods/reactions.list
   */
  def listReactions(req: ListReactionsRequest): Request[ListReactionsResponse, AccessToken] =
    request("reactions.list").formBody(req).as[ListReactionsResponse].auth.accessToken

  /**
   * Removes a reaction from an item.
   * @see https://api.slack.com/methods/reactions.remove
   */
  def removeReactions(req: RemoveReactionsRequest): Request[Unit, AccessToken] =
    request("reactions.remove").jsonBody(req).auth.accessToken

}
