/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedCalls {

  /**
   * Registers a new Call.
   * @see https://api.slack.com/methods/calls.add
   */
  def addCalls(req: AddCallsRequest): Request[AddCallsResponse, AccessToken] =
    request("calls.add").jsonBody(req).as[AddCallsResponse].auth.accessToken

  /**
   * Ends a Call.
   * @see https://api.slack.com/methods/calls.end
   */
  def endCalls(req: EndCallsRequest): Request[EndCallsResponse, AccessToken] =
    request("calls.end").jsonBody(req).as[EndCallsResponse].auth.accessToken

  /**
   * Returns information about a Call.
   * @see https://api.slack.com/methods/calls.info
   */
  def infoCalls(req: InfoCallsRequest): Request[InfoCallsResponse, AccessToken] =
    request("calls.info").jsonBody(req).as[InfoCallsResponse].auth.accessToken

  /**
   * Registers new participants added to a Call.
   * @see https://api.slack.com/methods/calls.participants.add
   */
  def addParticipantsCalls(req: AddParticipantsCallsRequest): Request[AddParticipantsCallsResponse, AccessToken] =
    request("calls.participants.add").jsonBody(req).as[AddParticipantsCallsResponse].auth.accessToken

  /**
   * Registers participants removed from a Call.
   * @see https://api.slack.com/methods/calls.participants.remove
   */
  def removeParticipantsCalls(
    req: RemoveParticipantsCallsRequest
  ): Request[RemoveParticipantsCallsResponse, AccessToken] =
    request("calls.participants.remove").jsonBody(req).as[RemoveParticipantsCallsResponse].auth.accessToken

  /**
   * Updates information about a Call.
   * @see https://api.slack.com/methods/calls.update
   */
  def updateCalls(req: UpdateCallsRequest): Request[UpdateCallsResponse, AccessToken] =
    request("calls.update").jsonBody(req).as[UpdateCallsResponse].auth.accessToken

}
