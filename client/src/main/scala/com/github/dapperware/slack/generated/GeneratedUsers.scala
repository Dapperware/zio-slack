/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedUsers {

  /**
   * List conversations the calling user may access.
   * @see https://api.slack.com/methods/users.conversations
   */
  def conversationsUsers(req: ConversationsUsersRequest): Request[ConversationsUsersResponse, AccessToken] =
    request("users.conversations").formBody(req).as[ConversationsUsersResponse].auth.accessToken

  /**
   * Delete the user profile photo
   * @see https://api.slack.com/methods/users.deletePhoto
   */
  def deletePhotoUsers: Request[Unit, AccessToken] =
    request("users.deletePhoto").auth.accessToken

  /**
   * Gets user presence information.
   * @see https://api.slack.com/methods/users.getPresence
   */
  def getPresenceUsers(req: GetPresenceUsersRequest): Request[GetPresenceUsersResponse, AccessToken] =
    request("users.getPresence").formBody(req).as[GetPresenceUsersResponse].auth.accessToken

  /**
   * Get a user's identity.
   * @see https://api.slack.com/methods/users.identity
   */
  def identityUsers: Request[Unit, AccessToken] =
    request("users.identity").auth.accessToken

  /**
   * Gets information about a user.
   * @see https://api.slack.com/methods/users.info
   */
  def infoUsers(req: InfoUsersRequest): Request[InfoUsersResponse, AccessToken] =
    request("users.info").formBody(req).as[InfoUsersResponse].auth.accessToken

  /**
   * Lists all users in a Slack team.
   * @see https://api.slack.com/methods/users.list
   */
  def listUsers(req: ListUsersRequest): Request[ListUsersResponse, AccessToken] =
    request("users.list").formBody(req).as[ListUsersResponse].auth.accessToken

  /**
   * Find a user with an email address.
   * @see https://api.slack.com/methods/users.lookupByEmail
   */
  def lookupByEmailUsers(req: LookupByEmailUsersRequest): Request[LookupByEmailUsersResponse, AccessToken] =
    request("users.lookupByEmail").formBody(req).as[LookupByEmailUsersResponse].auth.accessToken

  /**
   * Marked a user as active. Deprecated and non-functional.
   * @see https://api.slack.com/methods/users.setActive
   */
  def setActiveUsers: Request[Unit, AccessToken] =
    request("users.setActive").auth.accessToken

  /**
   * Manually sets user presence.
   * @see https://api.slack.com/methods/users.setPresence
   */
  def setPresenceUsers(req: SetPresenceUsersRequest): Request[Unit, AccessToken] =
    request("users.setPresence").jsonBody(req).auth.accessToken

}
