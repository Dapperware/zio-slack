/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedFiles {

  /**
   * Deletes an existing comment on a file.
   * @see https://api.slack.com/methods/files.comments.delete
   */
  def deleteCommentsFiles(req: DeleteCommentsFilesRequest): Request[Unit, AccessToken] =
    request("files.comments.delete").jsonBody(req).auth.accessToken

  /**
   * Deletes a file.
   * @see https://api.slack.com/methods/files.delete
   */
  def deleteFiles(req: DeleteFilesRequest): Request[Unit, AccessToken] =
    request("files.delete").jsonBody(req).auth.accessToken

  /**
   * List for a team, in a channel, or from a user with applied filters.
   * @see https://api.slack.com/methods/files.list
   */
  def listFiles(req: ListFilesRequest): Request[ListFilesResponse, AccessToken] =
    request("files.list").formBody(req).as[ListFilesResponse].auth.accessToken

  /**
   * Adds a file from a remote service
   * @see https://api.slack.com/methods/files.remote.add
   */
  def addRemoteFiles(req: AddRemoteFilesRequest): Request[AddRemoteFilesResponse, AccessToken] =
    request("files.remote.add").formBody(req).as[AddRemoteFilesResponse].auth.accessToken

  /**
   * Retrieve information about a remote file added to Slack
   * @see https://api.slack.com/methods/files.remote.info
   */
  def infoRemoteFiles(req: InfoRemoteFilesRequest): Request[InfoRemoteFilesResponse, AccessToken] =
    request("files.remote.info").formBody(req).as[InfoRemoteFilesResponse].auth.accessToken

  /**
   * Retrieve information about a remote file added to Slack
   * @see https://api.slack.com/methods/files.remote.list
   */
  def listRemoteFiles(req: ListRemoteFilesRequest): Request[ListRemoteFilesResponse, AccessToken] =
    request("files.remote.list").formBody(req).as[ListRemoteFilesResponse].auth.accessToken

  /**
   * Remove a remote file.
   * @see https://api.slack.com/methods/files.remote.remove
   */
  def removeRemoteFiles(req: RemoveRemoteFilesRequest): Request[Unit, AccessToken] =
    request("files.remote.remove").formBody(req).auth.accessToken

  /**
   * Share a remote file into a channel.
   * @see https://api.slack.com/methods/files.remote.share
   */
  def shareRemoteFiles(req: ShareRemoteFilesRequest): Request[ShareRemoteFilesResponse, AccessToken] =
    request("files.remote.share").formBody(req).as[ShareRemoteFilesResponse].auth.accessToken

  /**
   * Updates an existing remote file.
   * @see https://api.slack.com/methods/files.remote.update
   */
  def updateRemoteFiles(req: UpdateRemoteFilesRequest): Request[UpdateRemoteFilesResponse, AccessToken] =
    request("files.remote.update").formBody(req).as[UpdateRemoteFilesResponse].auth.accessToken

}
