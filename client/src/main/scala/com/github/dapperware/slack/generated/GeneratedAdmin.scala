/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedAdmin {

  /**
   * Approve an app for installation on a workspace.
   * @see https://api.slack.com/methods/admin.apps.approve
   */
  def approveAppsAdmin(req: ApproveAppsAdminRequest): Request[Unit, AccessToken] =
    request("admin.apps.approve").jsonBody(req).auth.accessToken

  /**
   * List approved apps for an org or workspace.
   * @see https://api.slack.com/methods/admin.apps.approved.list
   */
  def listApprovedAppsAdmin(req: ListApprovedAppsAdminRequest): Request[Unit, AccessToken] =
    request("admin.apps.approved.list").formBody(req).auth.accessToken

  /**
   * List app requests for a team/workspace.
   * @see https://api.slack.com/methods/admin.apps.requests.list
   */
  def listRequestsAppsAdmin(req: ListRequestsAppsAdminRequest): Request[Unit, AccessToken] =
    request("admin.apps.requests.list").formBody(req).auth.accessToken

  /**
   * Restrict an app for installation on a workspace.
   * @see https://api.slack.com/methods/admin.apps.restrict
   */
  def restrictAppsAdmin(req: RestrictAppsAdminRequest): Request[Unit, AccessToken] =
    request("admin.apps.restrict").jsonBody(req).auth.accessToken

  /**
   * List restricted apps for an org or workspace.
   * @see https://api.slack.com/methods/admin.apps.restricted.list
   */
  def listRestrictedAppsAdmin(req: ListRestrictedAppsAdminRequest): Request[Unit, AccessToken] =
    request("admin.apps.restricted.list").formBody(req).auth.accessToken

  /**
   * Archive a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.archive
   */
  def archiveConversationsAdmin(req: ArchiveConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.archive").jsonBody(req).auth.accessToken

  /**
   * Convert a public channel to a private channel.
   * @see https://api.slack.com/methods/admin.conversations.convertToPrivate
   */
  def convertToPrivateConversationsAdmin(req: ConvertToPrivateConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.convertToPrivate").jsonBody(req).auth.accessToken

  /**
   * Create a public or private channel-based conversation.
   * @see https://api.slack.com/methods/admin.conversations.create
   */
  def createConversationsAdmin(
    req: CreateConversationsAdminRequest
  ): Request[CreateConversationsAdminResponse, AccessToken] =
    request("admin.conversations.create").jsonBody(req).as[CreateConversationsAdminResponse].auth.accessToken

  /**
   * Delete a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.delete
   */
  def deleteConversationsAdmin(req: DeleteConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.delete").jsonBody(req).auth.accessToken

  /**
   * Disconnect a connected channel from one or more workspaces.
   * @see https://api.slack.com/methods/admin.conversations.disconnectShared
   */
  def disconnectSharedConversationsAdmin(req: DisconnectSharedConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.disconnectShared").jsonBody(req).auth.accessToken

  /**
   * List all disconnected channels—i.e., channels that were once connected to other workspaces and then disconnected—and the corresponding original channel IDs for key revocation with EKM.
   * @see https://api.slack.com/methods/admin.conversations.ekm.listOriginalConnectedChannelInfo
   */
  def listOriginalConnectedChannelInfoEkmConversationsAdmin(
    req: ListOriginalConnectedChannelInfoEkmConversationsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.conversations.ekm.listOriginalConnectedChannelInfo").formBody(req).auth.accessToken

  /**
   * Get all the workspaces a given public or private channel is connected to within this Enterprise org.
   * @see https://api.slack.com/methods/admin.conversations.getTeams
   */
  def getTeamsConversationsAdmin(
    req: GetTeamsConversationsAdminRequest
  ): Request[GetTeamsConversationsAdminResponse, AccessToken] =
    request("admin.conversations.getTeams").jsonBody(req).as[GetTeamsConversationsAdminResponse].auth.accessToken

  /**
   * Invite a user to a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.invite
   */
  def inviteConversationsAdmin(req: InviteConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.invite").jsonBody(req).auth.accessToken

  /**
   * Rename a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.rename
   */
  def renameConversationsAdmin(req: RenameConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.rename").jsonBody(req).auth.accessToken

  /**
   * Add an allowlist of IDP groups for accessing a channel
   * @see https://api.slack.com/methods/admin.conversations.restrictAccess.addGroup
   */
  def addGroupRestrictAccessConversationsAdmin(
    req: AddGroupRestrictAccessConversationsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.conversations.restrictAccess.addGroup").formBody(req).auth.accessToken

  /**
   * List all IDP Groups linked to a channel
   * @see https://api.slack.com/methods/admin.conversations.restrictAccess.listGroups
   */
  def listGroupsRestrictAccessConversationsAdmin(
    req: ListGroupsRestrictAccessConversationsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.conversations.restrictAccess.listGroups").formBody(req).auth.accessToken

  /**
   * Remove a linked IDP group linked from a private channel
   * @see https://api.slack.com/methods/admin.conversations.restrictAccess.removeGroup
   */
  def removeGroupRestrictAccessConversationsAdmin(
    req: RemoveGroupRestrictAccessConversationsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.conversations.restrictAccess.removeGroup").formBody(req).auth.accessToken

  /**
   * Search for public or private channels in an Enterprise organization.
   * @see https://api.slack.com/methods/admin.conversations.search
   */
  def searchConversationsAdmin(
    req: SearchConversationsAdminRequest
  ): Request[SearchConversationsAdminResponse, AccessToken] =
    request("admin.conversations.search").jsonBody(req).as[SearchConversationsAdminResponse].auth.accessToken

  /**
   * Set the posting permissions for a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.setConversationPrefs
   */
  def setConversationPrefsConversationsAdmin(
    req: SetConversationPrefsConversationsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.conversations.setConversationPrefs").jsonBody(req).auth.accessToken

  /**
   * Set the workspaces in an Enterprise grid org that connect to a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.setTeams
   */
  def setTeamsConversationsAdmin(req: SetTeamsConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.setTeams").jsonBody(req).auth.accessToken

  /**
   * Unarchive a public or private channel.
   * @see https://api.slack.com/methods/admin.conversations.unarchive
   */
  def unarchiveConversationsAdmin(req: UnarchiveConversationsAdminRequest): Request[Unit, AccessToken] =
    request("admin.conversations.unarchive").jsonBody(req).auth.accessToken

  /**
   * Add an emoji.
   * @see https://api.slack.com/methods/admin.emoji.add
   */
  def addEmojiAdmin(req: AddEmojiAdminRequest): Request[Unit, AccessToken] =
    request("admin.emoji.add").formBody(req).auth.accessToken

  /**
   * Add an emoji alias.
   * @see https://api.slack.com/methods/admin.emoji.addAlias
   */
  def addAliasEmojiAdmin(req: AddAliasEmojiAdminRequest): Request[Unit, AccessToken] =
    request("admin.emoji.addAlias").formBody(req).auth.accessToken

  /**
   * List emoji for an Enterprise Grid organization.
   * @see https://api.slack.com/methods/admin.emoji.list
   */
  def listEmojiAdmin(req: ListEmojiAdminRequest): Request[Unit, AccessToken] =
    request("admin.emoji.list").formBody(req).auth.accessToken

  /**
   * Remove an emoji across an Enterprise Grid organization
   * @see https://api.slack.com/methods/admin.emoji.remove
   */
  def removeEmojiAdmin(req: RemoveEmojiAdminRequest): Request[Unit, AccessToken] =
    request("admin.emoji.remove").formBody(req).auth.accessToken

  /**
   * Rename an emoji.
   * @see https://api.slack.com/methods/admin.emoji.rename
   */
  def renameEmojiAdmin(req: RenameEmojiAdminRequest): Request[Unit, AccessToken] =
    request("admin.emoji.rename").formBody(req).auth.accessToken

  /**
   * Approve a workspace invite request.
   * @see https://api.slack.com/methods/admin.inviteRequests.approve
   */
  def approveInviteRequestsAdmin(req: ApproveInviteRequestsAdminRequest): Request[Unit, AccessToken] =
    request("admin.inviteRequests.approve").jsonBody(req).auth.accessToken

  /**
   * List all approved workspace invite requests.
   * @see https://api.slack.com/methods/admin.inviteRequests.approved.list
   */
  def listApprovedInviteRequestsAdmin(req: ListApprovedInviteRequestsAdminRequest): Request[Unit, AccessToken] =
    request("admin.inviteRequests.approved.list").jsonBody(req).auth.accessToken

  /**
   * List all denied workspace invite requests.
   * @see https://api.slack.com/methods/admin.inviteRequests.denied.list
   */
  def listDeniedInviteRequestsAdmin(req: ListDeniedInviteRequestsAdminRequest): Request[Unit, AccessToken] =
    request("admin.inviteRequests.denied.list").jsonBody(req).auth.accessToken

  /**
   * Deny a workspace invite request.
   * @see https://api.slack.com/methods/admin.inviteRequests.deny
   */
  def denyInviteRequestsAdmin(req: DenyInviteRequestsAdminRequest): Request[Unit, AccessToken] =
    request("admin.inviteRequests.deny").jsonBody(req).auth.accessToken

  /**
   * List all pending workspace invite requests.
   * @see https://api.slack.com/methods/admin.inviteRequests.list
   */
  def listInviteRequestsAdmin(req: ListInviteRequestsAdminRequest): Request[Unit, AccessToken] =
    request("admin.inviteRequests.list").jsonBody(req).auth.accessToken

  /**
   * List all of the admins on a given workspace.
   * @see https://api.slack.com/methods/admin.teams.admins.list
   */
  def listAdminsTeamsAdmin(req: ListAdminsTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.admins.list").formBody(req).auth.accessToken

  /**
   * Create an Enterprise team.
   * @see https://api.slack.com/methods/admin.teams.create
   */
  def createTeamsAdmin(req: CreateTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.create").jsonBody(req).auth.accessToken

  /**
   * List all teams on an Enterprise organization
   * @see https://api.slack.com/methods/admin.teams.list
   */
  def listTeamsAdmin(req: ListTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.list").jsonBody(req).auth.accessToken

  /**
   * List all of the owners on a given workspace.
   * @see https://api.slack.com/methods/admin.teams.owners.list
   */
  def listOwnersTeamsAdmin(req: ListOwnersTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.owners.list").formBody(req).auth.accessToken

  /**
   * Fetch information about settings in a workspace
   * @see https://api.slack.com/methods/admin.teams.settings.info
   */
  def infoSettingsTeamsAdmin(req: InfoSettingsTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.settings.info").jsonBody(req).auth.accessToken

  /**
   * Set the default channels of a workspace.
   * @see https://api.slack.com/methods/admin.teams.settings.setDefaultChannels
   */
  def setDefaultChannelsSettingsTeamsAdmin(
    req: SetDefaultChannelsSettingsTeamsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.teams.settings.setDefaultChannels").formBody(req).auth.accessToken

  /**
   * Set the description of a given workspace.
   * @see https://api.slack.com/methods/admin.teams.settings.setDescription
   */
  def setDescriptionSettingsTeamsAdmin(req: SetDescriptionSettingsTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.settings.setDescription").jsonBody(req).auth.accessToken

  /**
   * An API method that allows admins to set the discoverability of a given workspace
   * @see https://api.slack.com/methods/admin.teams.settings.setDiscoverability
   */
  def setDiscoverabilitySettingsTeamsAdmin(
    req: SetDiscoverabilitySettingsTeamsAdminRequest
  ): Request[Unit, AccessToken] =
    request("admin.teams.settings.setDiscoverability").jsonBody(req).auth.accessToken

  /**
   * Sets the icon of a workspace.
   * @see https://api.slack.com/methods/admin.teams.settings.setIcon
   */
  def setIconSettingsTeamsAdmin(req: SetIconSettingsTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.settings.setIcon").formBody(req).auth.accessToken

  /**
   * Set the name of a given workspace.
   * @see https://api.slack.com/methods/admin.teams.settings.setName
   */
  def setNameSettingsTeamsAdmin(req: SetNameSettingsTeamsAdminRequest): Request[Unit, AccessToken] =
    request("admin.teams.settings.setName").jsonBody(req).auth.accessToken

  /**
   * Add one or more default channels to an IDP group.
   * @see https://api.slack.com/methods/admin.usergroups.addChannels
   */
  def addChannelsUsergroupsAdmin(req: AddChannelsUsergroupsAdminRequest): Request[Unit, AccessToken] =
    request("admin.usergroups.addChannels").jsonBody(req).auth.accessToken

  /**
   * Associate one or more default workspaces with an organization-wide IDP group.
   * @see https://api.slack.com/methods/admin.usergroups.addTeams
   */
  def addTeamsUsergroupsAdmin(req: AddTeamsUsergroupsAdminRequest): Request[Unit, AccessToken] =
    request("admin.usergroups.addTeams").jsonBody(req).auth.accessToken

  /**
   * List the channels linked to an org-level IDP group (user group).
   * @see https://api.slack.com/methods/admin.usergroups.listChannels
   */
  def listChannelsUsergroupsAdmin(req: ListChannelsUsergroupsAdminRequest): Request[Unit, AccessToken] =
    request("admin.usergroups.listChannels").jsonBody(req).auth.accessToken

  /**
   * Remove one or more default channels from an org-level IDP group (user group).
   * @see https://api.slack.com/methods/admin.usergroups.removeChannels
   */
  def removeChannelsUsergroupsAdmin(req: RemoveChannelsUsergroupsAdminRequest): Request[Unit, AccessToken] =
    request("admin.usergroups.removeChannels").jsonBody(req).auth.accessToken

  /**
   * Add an Enterprise user to a workspace.
   * @see https://api.slack.com/methods/admin.users.assign
   */
  def assignUsersAdmin(req: AssignUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.assign").jsonBody(req).auth.accessToken

  /**
   * Invite a user to a workspace.
   * @see https://api.slack.com/methods/admin.users.invite
   */
  def inviteUsersAdmin(req: InviteUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.invite").jsonBody(req).auth.accessToken

  /**
   * List users on a workspace
   * @see https://api.slack.com/methods/admin.users.list
   */
  def listUsersAdmin(req: ListUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.list").jsonBody(req).auth.accessToken

  /**
   * Remove a user from a workspace.
   * @see https://api.slack.com/methods/admin.users.remove
   */
  def removeUsersAdmin(req: RemoveUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.remove").jsonBody(req).auth.accessToken

  /**
   * Invalidate a single session for a user by session_id
   * @see https://api.slack.com/methods/admin.users.session.invalidate
   */
  def invalidateSessionUsersAdmin(req: InvalidateSessionUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.session.invalidate").jsonBody(req).auth.accessToken

  /**
   * Wipes all valid sessions on all devices for a given user
   * @see https://api.slack.com/methods/admin.users.session.reset
   */
  def resetSessionUsersAdmin(req: ResetSessionUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.session.reset").jsonBody(req).auth.accessToken

  /**
   * Set an existing guest, regular user, or owner to be an admin user.
   * @see https://api.slack.com/methods/admin.users.setAdmin
   */
  def setAdminUsersAdmin(req: SetAdminUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.setAdmin").jsonBody(req).auth.accessToken

  /**
   * Set an expiration for a guest user
   * @see https://api.slack.com/methods/admin.users.setExpiration
   */
  def setExpirationUsersAdmin(req: SetExpirationUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.setExpiration").jsonBody(req).auth.accessToken

  /**
   * Set an existing guest, regular user, or admin user to be a workspace owner.
   * @see https://api.slack.com/methods/admin.users.setOwner
   */
  def setOwnerUsersAdmin(req: SetOwnerUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.setOwner").jsonBody(req).auth.accessToken

  /**
   * Set an existing guest user, admin user, or owner to be a regular user.
   * @see https://api.slack.com/methods/admin.users.setRegular
   */
  def setRegularUsersAdmin(req: SetRegularUsersAdminRequest): Request[Unit, AccessToken] =
    request("admin.users.setRegular").jsonBody(req).auth.accessToken

}
