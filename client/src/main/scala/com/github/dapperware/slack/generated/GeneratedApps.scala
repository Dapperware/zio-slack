/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedApps {

  /**
   * Get a list of authorizations for the given event context. Each authorization represents an app installation that the event is visible to.
   * @see https://api.slack.com/methods/apps.event.authorizations.list
   */
  def listAuthorizationsEventApps(req: ListAuthorizationsEventAppsRequest): Request[Unit, AccessToken] =
    request("apps.event.authorizations.list").jsonBody(req).auth.accessToken

  /**
   * Allows an app to request additional scopes
   * @see https://api.slack.com/methods/apps.permissions.request
   */
  def requestPermissionsApps(req: RequestPermissionsAppsRequest): Request[Unit, AccessToken] =
    request("apps.permissions.request").formBody(req).auth.accessToken

  /**
   * Returns list of resource grants this app has on a team.
   * @see https://api.slack.com/methods/apps.permissions.resources.list
   */
  def listResourcesPermissionsApps(
    req: ListResourcesPermissionsAppsRequest
  ): Request[ListResourcesPermissionsAppsResponse, AccessToken] =
    request("apps.permissions.resources.list").formBody(req).as[ListResourcesPermissionsAppsResponse].auth.accessToken

  /**
   * Uninstalls your app from a workspace.
   * @see https://api.slack.com/methods/apps.uninstall
   */
  def uninstallApps: Request[Unit, AccessToken] =
    request("apps.uninstall").auth.accessToken

}
