/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedUsergroups {

  /**
   * Create a User Group
   * @see https://api.slack.com/methods/usergroups.create
   */
  def createUsergroups(req: CreateUsergroupsRequest): Request[CreateUsergroupsResponse, AccessToken] =
    request("usergroups.create").jsonBody(req).as[CreateUsergroupsResponse].auth.accessToken

  /**
   * Disable an existing User Group
   * @see https://api.slack.com/methods/usergroups.disable
   */
  def disableUsergroups(req: DisableUsergroupsRequest): Request[DisableUsergroupsResponse, AccessToken] =
    request("usergroups.disable").jsonBody(req).as[DisableUsergroupsResponse].auth.accessToken

  /**
   * Enable a User Group
   * @see https://api.slack.com/methods/usergroups.enable
   */
  def enableUsergroups(req: EnableUsergroupsRequest): Request[EnableUsergroupsResponse, AccessToken] =
    request("usergroups.enable").jsonBody(req).as[EnableUsergroupsResponse].auth.accessToken

  /**
   * List all User Groups for a team
   * @see https://api.slack.com/methods/usergroups.list
   */
  def listUsergroups(req: ListUsergroupsRequest): Request[ListUsergroupsResponse, AccessToken] =
    request("usergroups.list").formBody(req).as[ListUsergroupsResponse].auth.accessToken

  /**
   * Update an existing User Group
   * @see https://api.slack.com/methods/usergroups.update
   */
  def updateUsergroups(req: UpdateUsergroupsRequest): Request[UpdateUsergroupsResponse, AccessToken] =
    request("usergroups.update").jsonBody(req).as[UpdateUsergroupsResponse].auth.accessToken

  /**
   * List all users in a User Group
   * @see https://api.slack.com/methods/usergroups.users.list
   */
  def listUsersUsergroups(req: ListUsersUsergroupsRequest): Request[ListUsersUsergroupsResponse, AccessToken] =
    request("usergroups.users.list").formBody(req).as[ListUsersUsergroupsResponse].auth.accessToken

  /**
   * Update the list of users for a User Group
   * @see https://api.slack.com/methods/usergroups.users.update
   */
  def updateUsersUsergroups(req: UpdateUsersUsergroupsRequest): Request[UpdateUsersUsergroupsResponse, AccessToken] =
    request("usergroups.users.update").jsonBody(req).as[UpdateUsersUsergroupsResponse].auth.accessToken

}
