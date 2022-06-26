/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedPins {

  /**
   * Pins an item to a channel.
   * @see https://api.slack.com/methods/pins.add
   */
  def addPins(req: AddPinsRequest): Request[Unit, AccessToken] =
    request("pins.add").jsonBody(req).auth.accessToken

  /**
   * Lists items pinned to a channel.
   * @see https://api.slack.com/methods/pins.list
   */
  def listPins(req: ListPinsRequest): Request[ListPinsResponse, AccessToken] =
    request("pins.list").formBody(req).as[ListPinsResponse].auth.accessToken

  /**
   * Un-pins an item from a channel.
   * @see https://api.slack.com/methods/pins.remove
   */
  def removePins(req: RemovePinsRequest): Request[Unit, AccessToken] =
    request("pins.remove").jsonBody(req).auth.accessToken

}
