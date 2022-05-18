/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param app_id The id of the app to approve.
 * @param request_id The id of the request to approve.
 * @param team_id undefined
 */
case class ApproveAppsAdminRequest(
  app_id: Option[String] = None,
  request_id: Option[String] = None,
  team_id: Option[String] = None
)

object ApproveAppsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ApproveAppsAdminRequest] = deriveEncoder[ApproveAppsAdminRequest]
}

/**
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page
 * @param team_id undefined
 * @param enterprise_id undefined
 */
case class ListApprovedAppsAdminRequest(
  limit: Option[Int] = None,
  cursor: Option[String] = None,
  team_id: Option[String] = None,
  enterprise_id: Option[String] = None
)

object ListApprovedAppsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListApprovedAppsAdminRequest] =
    FormEncoder.fromParams.contramap[ListApprovedAppsAdminRequest] { req =>
      List("limit" -> req.limit, "cursor" -> req.cursor, "team_id" -> req.team_id, "enterprise_id" -> req.enterprise_id)
    }
}

/**
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page
 * @param team_id undefined
 */
case class ListRequestsAppsAdminRequest(
  limit: Option[Int] = None,
  cursor: Option[String] = None,
  team_id: Option[String] = None
)

object ListRequestsAppsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListRequestsAppsAdminRequest] =
    FormEncoder.fromParams.contramap[ListRequestsAppsAdminRequest] { req =>
      List("limit" -> req.limit, "cursor" -> req.cursor, "team_id" -> req.team_id)
    }
}

/**
 * @param app_id The id of the app to restrict.
 * @param request_id The id of the request to restrict.
 * @param team_id undefined
 */
case class RestrictAppsAdminRequest(
  app_id: Option[String] = None,
  request_id: Option[String] = None,
  team_id: Option[String] = None
)

object RestrictAppsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RestrictAppsAdminRequest] = deriveEncoder[RestrictAppsAdminRequest]
}

/**
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page
 * @param team_id undefined
 * @param enterprise_id undefined
 */
case class ListRestrictedAppsAdminRequest(
  limit: Option[Int] = None,
  cursor: Option[String] = None,
  team_id: Option[String] = None,
  enterprise_id: Option[String] = None
)

object ListRestrictedAppsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListRestrictedAppsAdminRequest] =
    FormEncoder.fromParams.contramap[ListRestrictedAppsAdminRequest] { req =>
      List("limit" -> req.limit, "cursor" -> req.cursor, "team_id" -> req.team_id, "enterprise_id" -> req.enterprise_id)
    }
}

/**
 * @param channel_id The channel to archive.
 */
case class ArchiveConversationsAdminRequest(channel_id: String)

object ArchiveConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ArchiveConversationsAdminRequest] = deriveEncoder[ArchiveConversationsAdminRequest]
}

/**
 * @param channel_id The channel to convert to private.
 */
case class ConvertToPrivateConversationsAdminRequest(channel_id: String)

object ConvertToPrivateConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ConvertToPrivateConversationsAdminRequest] =
    deriveEncoder[ConvertToPrivateConversationsAdminRequest]
}

/**
 * @param name Name of the public or private channel to create.
 * @param is_private When `true`, creates a private channel instead of a public channel
 * @param description Description of the public or private channel to create.
 * @param org_wide When `true`, the channel will be available org-wide. Note: if the channel is not `org_wide=true`, you must specify a `team_id` for this channel
 * @param team_id The workspace to create the channel in. Note: this argument is required unless you set `org_wide=true`.
 */
case class CreateConversationsAdminRequest(
  name: String,
  is_private: Boolean,
  description: Option[String] = None,
  org_wide: Option[Boolean] = None,
  team_id: Option[String] = None
)

object CreateConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[CreateConversationsAdminRequest] = deriveEncoder[CreateConversationsAdminRequest]
}

/**
 * @param channel_id The channel to delete.
 */
case class DeleteConversationsAdminRequest(channel_id: String)

object DeleteConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteConversationsAdminRequest] = deriveEncoder[DeleteConversationsAdminRequest]
}

/**
 * @param channel_id The channel to be disconnected from some workspaces.
 * @param leaving_team_ids The team to be removed from the channel. Currently only a single team id can be specified.
 */
case class DisconnectSharedConversationsAdminRequest(channel_id: String, leaving_team_ids: Option[String] = None)

object DisconnectSharedConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DisconnectSharedConversationsAdminRequest] =
    deriveEncoder[DisconnectSharedConversationsAdminRequest]
}

/**
 * @param channel_ids A comma-separated list of channels to filter to.
 * @param team_ids A comma-separated list of the workspaces to which the channels you would like returned belong.
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 */
case class ListOriginalConnectedChannelInfoEkmConversationsAdminRequest(
  channel_ids: Option[String] = None,
  team_ids: Option[String] = None,
  limit: Option[Int] = None,
  cursor: Option[String] = None
)

object ListOriginalConnectedChannelInfoEkmConversationsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListOriginalConnectedChannelInfoEkmConversationsAdminRequest] =
    FormEncoder.fromParams.contramap[ListOriginalConnectedChannelInfoEkmConversationsAdminRequest] { req =>
      List("channel_ids" -> req.channel_ids, "team_ids" -> req.team_ids, "limit" -> req.limit, "cursor" -> req.cursor)
    }
}

/**
 * @param channel_id The channel to determine connected workspaces within the organization for.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 */
case class GetTeamsConversationsAdminRequest(
  channel_id: String,
  cursor: Option[String] = None,
  limit: Option[Int] = None
)

object GetTeamsConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[GetTeamsConversationsAdminRequest] = deriveEncoder[GetTeamsConversationsAdminRequest]
}

/**
 * @param user_ids The users to invite.
 * @param channel_id The channel that the users will be invited to.
 */
case class InviteConversationsAdminRequest(user_ids: String, channel_id: String)

object InviteConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[InviteConversationsAdminRequest] = deriveEncoder[InviteConversationsAdminRequest]
}

/**
 * @param channel_id The channel to rename.
 * @param name undefined
 */
case class RenameConversationsAdminRequest(channel_id: String, name: String)

object RenameConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RenameConversationsAdminRequest] = deriveEncoder[RenameConversationsAdminRequest]
}

/**
 * @param group_id The [IDP Group](https://slack.com/help/articles/115001435788-Connect-identity-provider-groups-to-your-Enterprise-Grid-org) ID to be an allowlist for the private channel.
 * @param channel_id The channel to link this group to.
 * @param team_id The workspace where the channel exists. This argument is required for channels only tied to one workspace, and optional for channels that are shared across an organization.
 */
case class AddGroupRestrictAccessConversationsAdminRequest(
  group_id: String,
  channel_id: String,
  team_id: Option[String] = None
)

object AddGroupRestrictAccessConversationsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AddGroupRestrictAccessConversationsAdminRequest] =
    FormEncoder.fromParams.contramap[AddGroupRestrictAccessConversationsAdminRequest] { req =>
      List("group_id" -> req.group_id, "channel_id" -> req.channel_id, "team_id" -> req.team_id)
    }
}

/**
 * @param channel_id undefined
 * @param team_id The workspace where the channel exists. This argument is required for channels only tied to one workspace, and optional for channels that are shared across an organization.
 */
case class ListGroupsRestrictAccessConversationsAdminRequest(channel_id: String, team_id: Option[String] = None)

object ListGroupsRestrictAccessConversationsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListGroupsRestrictAccessConversationsAdminRequest] =
    FormEncoder.fromParams.contramap[ListGroupsRestrictAccessConversationsAdminRequest] { req =>
      List("channel_id" -> req.channel_id, "team_id" -> req.team_id)
    }
}

/**
 * @param team_id The workspace where the channel exists. This argument is required for channels only tied to one workspace, and optional for channels that are shared across an organization.
 * @param group_id The [IDP Group](https://slack.com/help/articles/115001435788-Connect-identity-provider-groups-to-your-Enterprise-Grid-org) ID to remove from the private channel.
 * @param channel_id The channel to remove the linked group from.
 */
case class RemoveGroupRestrictAccessConversationsAdminRequest(team_id: String, group_id: String, channel_id: String)

object RemoveGroupRestrictAccessConversationsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RemoveGroupRestrictAccessConversationsAdminRequest] =
    FormEncoder.fromParams.contramap[RemoveGroupRestrictAccessConversationsAdminRequest] { req =>
      List("team_id" -> req.team_id, "group_id" -> req.group_id, "channel_id" -> req.channel_id)
    }
}

/**
 * @param team_ids Comma separated string of team IDs, signifying the workspaces to search through.
 * @param query Name of the the channel to query by.
 * @param limit Maximum number of items to be returned. Must be between 1 - 20 both inclusive. Default is 10.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 * @param search_channel_types The type of channel to include or exclude in the search. For example `private` will search private channels, while `private_exclude` will exclude them. For a full list of types, check the [Types section](#types).
 * @param sort Possible values are `relevant` (search ranking based on what we think is closest), `name` (alphabetical), `member_count` (number of users in the channel), and `created` (date channel was created). You can optionally pair this with the `sort_dir` arg to change how it is sorted
 * @param sort_dir Sort direction. Possible values are `asc` for ascending order like (1, 2, 3) or (a, b, c), and `desc` for descending order like (3, 2, 1) or (c, b, a)
 */
case class SearchConversationsAdminRequest(
  team_ids: Option[String] = None,
  query: Option[String] = None,
  limit: Option[Int] = None,
  cursor: Option[String] = None,
  search_channel_types: Option[String] = None,
  sort: Option[String] = None,
  sort_dir: Option[String] = None
)

object SearchConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SearchConversationsAdminRequest] = deriveEncoder[SearchConversationsAdminRequest]
}

/**
 * @param channel_id The channel to set the prefs for
 * @param prefs The prefs for this channel in a stringified JSON format.
 */
case class SetConversationPrefsConversationsAdminRequest(channel_id: String, prefs: String)

object SetConversationPrefsConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetConversationPrefsConversationsAdminRequest] =
    deriveEncoder[SetConversationPrefsConversationsAdminRequest]
}

/**
 * @param channel_id The encoded `channel_id` to add or remove to workspaces.
 * @param team_id The workspace to which the channel belongs. Omit this argument if the channel is a cross-workspace shared channel.
 * @param target_team_ids A comma-separated list of workspaces to which the channel should be shared. Not required if the channel is being shared org-wide.
 * @param org_channel True if channel has to be converted to an org channel
 */
case class SetTeamsConversationsAdminRequest(
  channel_id: String,
  team_id: Option[String] = None,
  target_team_ids: Option[String] = None,
  org_channel: Option[Boolean] = None
)

object SetTeamsConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetTeamsConversationsAdminRequest] = deriveEncoder[SetTeamsConversationsAdminRequest]
}

/**
 * @param channel_id The channel to unarchive.
 */
case class UnarchiveConversationsAdminRequest(channel_id: String)

object UnarchiveConversationsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[UnarchiveConversationsAdminRequest] = deriveEncoder[UnarchiveConversationsAdminRequest]
}

/**
 * @param name The name of the emoji to be removed. Colons (`:myemoji:`) around the value are not required, although they may be included.
 * @param url The URL of a file to use as an image for the emoji. Square images under 128KB and with transparent backgrounds work best.
 */
case class AddEmojiAdminRequest(name: String, url: String)

object AddEmojiAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AddEmojiAdminRequest] = FormEncoder.fromParams.contramap[AddEmojiAdminRequest] {
    req =>
      List("name" -> req.name, "url" -> req.url)
  }
}

/**
 * @param name The name of the emoji to be aliased. Colons (`:myemoji:`) around the value are not required, although they may be included.
 * @param alias_for The alias of the emoji.
 */
case class AddAliasEmojiAdminRequest(name: String, alias_for: String)

object AddAliasEmojiAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AddAliasEmojiAdminRequest] =
    FormEncoder.fromParams.contramap[AddAliasEmojiAdminRequest] { req =>
      List("name" -> req.name, "alias_for" -> req.alias_for)
    }
}

/**
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 */
case class ListEmojiAdminRequest(cursor: Option[String] = None, limit: Option[Int] = None)

object ListEmojiAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListEmojiAdminRequest] = FormEncoder.fromParams.contramap[ListEmojiAdminRequest] {
    req =>
      List("cursor" -> req.cursor, "limit" -> req.limit)
  }
}

/**
 * @param name The name of the emoji to be removed. Colons (`:myemoji:`) around the value are not required, although they may be included.
 */
case class RemoveEmojiAdminRequest(name: String)

object RemoveEmojiAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RemoveEmojiAdminRequest] =
    FormEncoder.fromParams.contramap[RemoveEmojiAdminRequest] { req =>
      List("name" -> req.name)
    }
}

/**
 * @param name The name of the emoji to be renamed. Colons (`:myemoji:`) around the value are not required, although they may be included.
 * @param new_name The new name of the emoji.
 */
case class RenameEmojiAdminRequest(name: String, new_name: String)

object RenameEmojiAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RenameEmojiAdminRequest] =
    FormEncoder.fromParams.contramap[RenameEmojiAdminRequest] { req =>
      List("name" -> req.name, "new_name" -> req.new_name)
    }
}

/**
 * @param invite_request_id ID of the request to invite.
 * @param team_id ID for the workspace where the invite request was made.
 */
case class ApproveInviteRequestsAdminRequest(invite_request_id: String, team_id: Option[String] = None)

object ApproveInviteRequestsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ApproveInviteRequestsAdminRequest] = deriveEncoder[ApproveInviteRequestsAdminRequest]
}

/**
 * @param team_id ID for the workspace where the invite requests were made.
 * @param cursor Value of the `next_cursor` field sent as part of the previous API response
 * @param limit The number of results that will be returned by the API on each invocation. Must be between 1 - 1000, both inclusive
 */
case class ListApprovedInviteRequestsAdminRequest(
  team_id: Option[String] = None,
  cursor: Option[String] = None,
  limit: Option[Int] = None
)

object ListApprovedInviteRequestsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListApprovedInviteRequestsAdminRequest] =
    deriveEncoder[ListApprovedInviteRequestsAdminRequest]
}

/**
 * @param team_id ID for the workspace where the invite requests were made.
 * @param cursor Value of the `next_cursor` field sent as part of the previous api response
 * @param limit The number of results that will be returned by the API on each invocation. Must be between 1 - 1000 both inclusive
 */
case class ListDeniedInviteRequestsAdminRequest(
  team_id: Option[String] = None,
  cursor: Option[String] = None,
  limit: Option[Int] = None
)

object ListDeniedInviteRequestsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListDeniedInviteRequestsAdminRequest] =
    deriveEncoder[ListDeniedInviteRequestsAdminRequest]
}

/**
 * @param invite_request_id ID of the request to invite.
 * @param team_id ID for the workspace where the invite request was made.
 */
case class DenyInviteRequestsAdminRequest(invite_request_id: String, team_id: Option[String] = None)

object DenyInviteRequestsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DenyInviteRequestsAdminRequest] = deriveEncoder[DenyInviteRequestsAdminRequest]
}

/**
 * @param team_id ID for the workspace where the invite requests were made.
 * @param cursor Value of the `next_cursor` field sent as part of the previous API response
 * @param limit The number of results that will be returned by the API on each invocation. Must be between 1 - 1000, both inclusive
 */
case class ListInviteRequestsAdminRequest(
  team_id: Option[String] = None,
  cursor: Option[String] = None,
  limit: Option[Int] = None
)

object ListInviteRequestsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListInviteRequestsAdminRequest] = deriveEncoder[ListInviteRequestsAdminRequest]
}

/**
 * @param team_id undefined
 * @param limit The maximum number of items to return.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 */
case class ListAdminsTeamsAdminRequest(team_id: String, limit: Option[Int] = None, cursor: Option[String] = None)

object ListAdminsTeamsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListAdminsTeamsAdminRequest] =
    FormEncoder.fromParams.contramap[ListAdminsTeamsAdminRequest] { req =>
      List("team_id" -> req.team_id, "limit" -> req.limit, "cursor" -> req.cursor)
    }
}

/**
 * @param team_domain Team domain (for example, slacksoftballteam).
 * @param team_name Team name (for example, Slack Softball Team).
 * @param team_description Description for the team.
 * @param team_discoverability Who can join the team. A team's discoverability can be `open`, `closed`, `invite_only`, or `unlisted`.
 */
case class CreateTeamsAdminRequest(
  team_domain: String,
  team_name: String,
  team_description: Option[String] = None,
  team_discoverability: Option[String] = None
)

object CreateTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[CreateTeamsAdminRequest] = deriveEncoder[CreateTeamsAdminRequest]
}

/**
 * @param limit The maximum number of items to return. Must be between 1 - 100 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 */
case class ListTeamsAdminRequest(limit: Option[Int] = None, cursor: Option[String] = None)

object ListTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListTeamsAdminRequest] = deriveEncoder[ListTeamsAdminRequest]
}

/**
 * @param team_id undefined
 * @param limit The maximum number of items to return. Must be between 1 - 1000 both inclusive.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 */
case class ListOwnersTeamsAdminRequest(team_id: String, limit: Option[Int] = None, cursor: Option[String] = None)

object ListOwnersTeamsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListOwnersTeamsAdminRequest] =
    FormEncoder.fromParams.contramap[ListOwnersTeamsAdminRequest] { req =>
      List("team_id" -> req.team_id, "limit" -> req.limit, "cursor" -> req.cursor)
    }
}

/**
 * @param team_id undefined
 */
case class InfoSettingsTeamsAdminRequest(team_id: String)

object InfoSettingsTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[InfoSettingsTeamsAdminRequest] = deriveEncoder[InfoSettingsTeamsAdminRequest]
}

/**
 * @param team_id ID for the workspace to set the default channel for.
 * @param channel_ids An array of channel IDs.
 */
case class SetDefaultChannelsSettingsTeamsAdminRequest(team_id: String, channel_ids: String)

object SetDefaultChannelsSettingsTeamsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[SetDefaultChannelsSettingsTeamsAdminRequest] =
    FormEncoder.fromParams.contramap[SetDefaultChannelsSettingsTeamsAdminRequest] { req =>
      List("team_id" -> req.team_id, "channel_ids" -> req.channel_ids)
    }
}

/**
 * @param team_id ID for the workspace to set the description for.
 * @param description The new description for the workspace.
 */
case class SetDescriptionSettingsTeamsAdminRequest(team_id: String, description: String)

object SetDescriptionSettingsTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetDescriptionSettingsTeamsAdminRequest] =
    deriveEncoder[SetDescriptionSettingsTeamsAdminRequest]
}

/**
 * @param team_id The ID of the workspace to set discoverability on.
 * @param discoverability This workspace's discovery setting. It must be set to one of `open`, `invite_only`, `closed`, or `unlisted`.
 */
case class SetDiscoverabilitySettingsTeamsAdminRequest(team_id: String, discoverability: String)

object SetDiscoverabilitySettingsTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetDiscoverabilitySettingsTeamsAdminRequest] =
    deriveEncoder[SetDiscoverabilitySettingsTeamsAdminRequest]
}

/**
 * @param image_url Image URL for the icon
 * @param team_id ID for the workspace to set the icon for.
 */
case class SetIconSettingsTeamsAdminRequest(image_url: String, team_id: String)

object SetIconSettingsTeamsAdminRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[SetIconSettingsTeamsAdminRequest] =
    FormEncoder.fromParams.contramap[SetIconSettingsTeamsAdminRequest] { req =>
      List("image_url" -> req.image_url, "team_id" -> req.team_id)
    }
}

/**
 * @param team_id ID for the workspace to set the name for.
 * @param name The new name of the workspace.
 */
case class SetNameSettingsTeamsAdminRequest(team_id: String, name: String)

object SetNameSettingsTeamsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetNameSettingsTeamsAdminRequest] = deriveEncoder[SetNameSettingsTeamsAdminRequest]
}

/**
 * @param usergroup_id ID of the IDP group to add default channels for.
 * @param channel_ids Comma separated string of channel IDs.
 * @param team_id The workspace to add default channels in.
 */
case class AddChannelsUsergroupsAdminRequest(usergroup_id: String, channel_ids: String, team_id: Option[String] = None)

object AddChannelsUsergroupsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddChannelsUsergroupsAdminRequest] = deriveEncoder[AddChannelsUsergroupsAdminRequest]
}

/**
 * @param usergroup_id An encoded usergroup (IDP Group) ID.
 * @param team_ids A comma separated list of encoded team (workspace) IDs. Each workspace *MUST* belong to the organization associated with the token.
 * @param auto_provision When `true`, this method automatically creates new workspace accounts for the IDP group members.
 */
case class AddTeamsUsergroupsAdminRequest(
  usergroup_id: String,
  team_ids: String,
  auto_provision: Option[Boolean] = None
)

object AddTeamsUsergroupsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddTeamsUsergroupsAdminRequest] = deriveEncoder[AddTeamsUsergroupsAdminRequest]
}

/**
 * @param usergroup_id ID of the IDP group to list default channels for.
 * @param team_id ID of the the workspace.
 * @param include_num_members Flag to include or exclude the count of members per channel.
 */
case class ListChannelsUsergroupsAdminRequest(
  usergroup_id: String,
  team_id: Option[String] = None,
  include_num_members: Option[Boolean] = None
)

object ListChannelsUsergroupsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListChannelsUsergroupsAdminRequest] = deriveEncoder[ListChannelsUsergroupsAdminRequest]
}

/**
 * @param usergroup_id ID of the IDP Group
 * @param channel_ids Comma-separated string of channel IDs
 */
case class RemoveChannelsUsergroupsAdminRequest(usergroup_id: String, channel_ids: String)

object RemoveChannelsUsergroupsAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RemoveChannelsUsergroupsAdminRequest] =
    deriveEncoder[RemoveChannelsUsergroupsAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id The ID of the user to add to the workspace.
 * @param is_restricted True if user should be added to the workspace as a guest.
 * @param is_ultra_restricted True if user should be added to the workspace as a single-channel guest.
 * @param channel_ids Comma separated values of channel IDs to add user in the new workspace.
 */
case class AssignUsersAdminRequest(
  team_id: String,
  user_id: String,
  is_restricted: Option[Boolean] = None,
  is_ultra_restricted: Option[Boolean] = None,
  channel_ids: Option[String] = None
)

object AssignUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AssignUsersAdminRequest] = deriveEncoder[AssignUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param email The email address of the person to invite.
 * @param channel_ids A comma-separated list of `channel_id`s for this user to join. At least one channel is required.
 * @param custom_message An optional message to send to the user in the invite email.
 * @param real_name Full name of the user.
 * @param resend Allow this invite to be resent in the future if a user has not signed up yet. (default: false)
 * @param is_restricted Is this user a multi-channel guest user? (default: false)
 * @param is_ultra_restricted Is this user a single channel guest user? (default: false)
 * @param guest_expiration_ts Timestamp when guest account should be disabled. Only include this timestamp if you are inviting a guest user and you want their account to expire on a certain date.
 */
case class InviteUsersAdminRequest(
  team_id: String,
  email: String,
  channel_ids: String,
  custom_message: Option[String] = None,
  real_name: Option[String] = None,
  resend: Option[Boolean] = None,
  is_restricted: Option[Boolean] = None,
  is_ultra_restricted: Option[Boolean] = None,
  guest_expiration_ts: Option[String] = None
)

object InviteUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[InviteUsersAdminRequest] = deriveEncoder[InviteUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param cursor Set `cursor` to `next_cursor` returned by the previous call to list items in the next page.
 * @param limit Limit for how many users to be retrieved per page
 */
case class ListUsersAdminRequest(team_id: String, cursor: Option[String] = None, limit: Option[Int] = None)

object ListUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListUsersAdminRequest] = deriveEncoder[ListUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id The ID of the user to remove.
 */
case class RemoveUsersAdminRequest(team_id: String, user_id: String)

object RemoveUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RemoveUsersAdminRequest] = deriveEncoder[RemoveUsersAdminRequest]
}

/**
 * @param team_id ID of the team that the session belongs to
 * @param session_id undefined
 */
case class InvalidateSessionUsersAdminRequest(team_id: String, session_id: Int)

object InvalidateSessionUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[InvalidateSessionUsersAdminRequest] = deriveEncoder[InvalidateSessionUsersAdminRequest]
}

/**
 * @param user_id The ID of the user to wipe sessions for
 * @param mobile_only Only expire mobile sessions (default: false)
 * @param web_only Only expire web sessions (default: false)
 */
case class ResetSessionUsersAdminRequest(
  user_id: String,
  mobile_only: Option[Boolean] = None,
  web_only: Option[Boolean] = None
)

object ResetSessionUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ResetSessionUsersAdminRequest] = deriveEncoder[ResetSessionUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id The ID of the user to designate as an admin.
 */
case class SetAdminUsersAdminRequest(team_id: String, user_id: String)

object SetAdminUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetAdminUsersAdminRequest] = deriveEncoder[SetAdminUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id The ID of the user to set an expiration for.
 * @param expiration_ts Timestamp when guest account should be disabled.
 */
case class SetExpirationUsersAdminRequest(team_id: String, user_id: String, expiration_ts: Int)

object SetExpirationUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetExpirationUsersAdminRequest] = deriveEncoder[SetExpirationUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id Id of the user to promote to owner.
 */
case class SetOwnerUsersAdminRequest(team_id: String, user_id: String)

object SetOwnerUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetOwnerUsersAdminRequest] = deriveEncoder[SetOwnerUsersAdminRequest]
}

/**
 * @param team_id The ID (`T1234`) of the workspace.
 * @param user_id The ID of the user to designate as a regular user.
 */
case class SetRegularUsersAdminRequest(team_id: String, user_id: String)

object SetRegularUsersAdminRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[SetRegularUsersAdminRequest] = deriveEncoder[SetRegularUsersAdminRequest]
}
