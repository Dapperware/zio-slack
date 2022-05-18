/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedUsergroups {

  /**
   * List all User Groups for a team
   * @see https://api.slack.com/methods/usergroups.list
   */
  def listUsergroups(req: ListUsergroupsRequest): Request[ListUsergroupsResponse, AccessToken] =
    request("usergroups.list").formBody(req).as[ListUsergroupsResponse].auth.accessToken

  /**
   * List all users in a User Group
   * @see https://api.slack.com/methods/usergroups.users.list
   */
  def listUsersUsergroups(req: ListUsersUsergroupsRequest): Request[ListUsersUsergroupsResponse, AccessToken] =
    request("usergroups.users.list").formBody(req).as[ListUsersUsergroupsResponse].auth.accessToken

}
