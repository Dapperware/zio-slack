/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedCalls {

  /**
   * Registers a new Call.
   * @see https://api.slack.com/methods/calls.add
   */
  def addCalls(req: AddCallsRequest): Request[Unit, AccessToken] =
    request("calls.add").jsonBody(req).auth.accessToken

  /**
   * Ends a Call.
   * @see https://api.slack.com/methods/calls.end
   */
  def endCalls(req: EndCallsRequest): Request[Unit, AccessToken] =
    request("calls.end").jsonBody(req).auth.accessToken

  /**
   * Returns information about a Call.
   * @see https://api.slack.com/methods/calls.info
   */
  def infoCalls(req: InfoCallsRequest): Request[Unit, AccessToken] =
    request("calls.info").jsonBody(req).auth.accessToken

  /**
   * Registers new participants added to a Call.
   * @see https://api.slack.com/methods/calls.participants.add
   */
  def addParticipantsCalls(req: AddParticipantsCallsRequest): Request[Unit, AccessToken] =
    request("calls.participants.add").jsonBody(req).auth.accessToken

  /**
   * Registers participants removed from a Call.
   * @see https://api.slack.com/methods/calls.participants.remove
   */
  def removeParticipantsCalls(req: RemoveParticipantsCallsRequest): Request[Unit, AccessToken] =
    request("calls.participants.remove").jsonBody(req).auth.accessToken

  /**
   * Updates information about a Call.
   * @see https://api.slack.com/methods/calls.update
   */
  def updateCalls(req: UpdateCallsRequest): Request[Unit, AccessToken] =
    request("calls.update").jsonBody(req).auth.accessToken

}
