/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param ts Timestamp of the message to be deleted.
 * @param channel Channel containing the message to be deleted.
 * @param as_user Pass true to delete the message as the authed user with `chat:write:user` scope. [Bot users](/bot-users) in this context are considered authed users. If unused or false, the message will be deleted with `chat:write:bot` scope.
 */
case class DeleteChatRequest(ts: String, channel: String, as_user: Option[Boolean])

object DeleteChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteChatRequest] = deriveEncoder[DeleteChatRequest]
}

/**
 * @param channel The channel the scheduled_message is posting to
 * @param scheduled_message_id `scheduled_message_id` returned from call to chat.scheduleMessage
 * @param as_user Pass true to delete the message as the authed user with `chat:write:user` scope. [Bot users](/bot-users) in this context are considered authed users. If unused or false, the message will be deleted with `chat:write:bot` scope.
 */
case class DeleteScheduledMessageChatRequest(channel: String, scheduled_message_id: String, as_user: Option[Boolean])

object DeleteScheduledMessageChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteScheduledMessageChatRequest] = deriveEncoder[DeleteScheduledMessageChatRequest]
}

/**
 * @param channel The ID of the conversation or channel containing the message
 * @param message_ts A message's `ts` value, uniquely identifying it within a channel
 */
case class GetPermalinkChatRequest(channel: String, message_ts: String)

object GetPermalinkChatRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[GetPermalinkChatRequest] =
    FormEncoder.fromParams.contramap[GetPermalinkChatRequest] { req =>
      List("channel" -> req.channel, "message_ts" -> req.message_ts)
    }
}

/**
 * @param channel Channel to send message to. Can be a public channel, private group or IM channel. Can be an encoded ID, or a name.
 * @param text Text of the message to send.
 */
case class MeMessageChatRequest(channel: Option[String], text: Option[String])

object MeMessageChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[MeMessageChatRequest] = deriveEncoder[MeMessageChatRequest]
}

/**
 * @param channel Channel, private group, or IM channel to send message to. Can be an encoded ID, or a name.
 * @param user `id` of the user who will receive the ephemeral message. The user should be in the channel specified by the `channel` argument.
 * @param as_user Pass true to post the message as the authed user. Defaults to true if the chat:write:bot scope is not included. Otherwise, defaults to false.
 * @param attachments A JSON-based array of structured attachments, presented as a URL-encoded string.
 * @param blocks A JSON-based array of structured blocks, presented as a URL-encoded string.
 * @param icon_emoji Emoji to use as the icon for this message. Overrides `icon_url`. Must be used in conjunction with `as_user` set to `false`, otherwise ignored. See [authorship](#authorship) below.
 * @param icon_url URL to an image to use as the icon for this message. Must be used in conjunction with `as_user` set to false, otherwise ignored. See [authorship](#authorship) below.
 * @param link_names Find and link channel names and usernames.
 * @param parse Change how messages are treated. Defaults to `none`. See [below](#formatting).
 * @param text How this field works and whether it is required depends on other fields you use in your API call. [See below](#text_usage) for more detail.
 * @param thread_ts Provide another message's `ts` value to post this message in a thread. Avoid using a reply's `ts` value; use its parent's value instead. Ephemeral messages in threads are only shown if there is already an active thread.
 * @param username Set your bot's user name. Must be used in conjunction with `as_user` set to false, otherwise ignored. See [authorship](#authorship) below.
 */
case class PostEphemeralChatRequest(
  channel: String,
  user: String,
  as_user: Option[Boolean],
  attachments: Option[String],
  blocks: Option[String],
  icon_emoji: Option[String],
  icon_url: Option[String],
  link_names: Option[Boolean],
  parse: Option[String],
  text: Option[String],
  thread_ts: Option[String],
  username: Option[String]
)

object PostEphemeralChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[PostEphemeralChatRequest] = deriveEncoder[PostEphemeralChatRequest]
}

/**
 * @param channel Channel, private group, or IM channel to send message to. Can be an encoded ID, or a name. See [below](#channels) for more details.
 * @param as_user Pass true to post the message as the authed user, instead of as a bot. Defaults to false. See [authorship](#authorship) below.
 * @param attachments A JSON-based array of structured attachments, presented as a URL-encoded string.
 * @param blocks A JSON-based array of structured blocks, presented as a URL-encoded string.
 * @param icon_emoji Emoji to use as the icon for this message. Overrides `icon_url`. Must be used in conjunction with `as_user` set to `false`, otherwise ignored. See [authorship](#authorship) below.
 * @param icon_url URL to an image to use as the icon for this message. Must be used in conjunction with `as_user` set to false, otherwise ignored. See [authorship](#authorship) below.
 * @param link_names Find and link channel names and usernames.
 * @param mrkdwn Disable Slack markup parsing by setting to `false`. Enabled by default.
 * @param parse Change how messages are treated. Defaults to `none`. See [below](#formatting).
 * @param reply_broadcast Used in conjunction with `thread_ts` and indicates whether reply should be made visible to everyone in the channel or conversation. Defaults to `false`.
 * @param text How this field works and whether it is required depends on other fields you use in your API call. [See below](#text_usage) for more detail.
 * @param thread_ts Provide another message's `ts` value to make this message a reply. Avoid using a reply's `ts` value; use its parent instead.
 * @param unfurl_links Pass true to enable unfurling of primarily text-based content.
 * @param unfurl_media Pass false to disable unfurling of media content.
 * @param username Set your bot's user name. Must be used in conjunction with `as_user` set to false, otherwise ignored. See [authorship](#authorship) below.
 */
case class PostMessageChatRequest(
  channel: String,
  as_user: Option[Boolean],
  attachments: Option[String],
  blocks: Option[String],
  icon_emoji: Option[String],
  icon_url: Option[String],
  link_names: Option[Boolean],
  mrkdwn: Option[Boolean],
  parse: Option[String],
  reply_broadcast: Option[Boolean],
  text: Option[String],
  thread_ts: Option[String],
  unfurl_links: Option[Boolean],
  unfurl_media: Option[Boolean],
  username: Option[String]
)

object PostMessageChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[PostMessageChatRequest] = deriveEncoder[PostMessageChatRequest]
}

/**
 * @param channel Channel, private group, or DM channel to send message to. Can be an encoded ID, or a name. See [below](#channels) for more details.
 * @param post_at Unix EPOCH timestamp of time in future to send the message.
 * @param text How this field works and whether it is required depends on other fields you use in your API call. [See below](#text_usage) for more detail.
 * @param parse Change how messages are treated. Defaults to `none`. See [chat.postMessage](chat.postMessage#formatting).
 * @param as_user Pass true to post the message as the authed user, instead of as a bot. Defaults to false. See [chat.postMessage](chat.postMessage#authorship).
 * @param link_names Find and link channel names and usernames.
 * @param attachments A JSON-based array of structured attachments, presented as a URL-encoded string.
 * @param blocks A JSON-based array of structured blocks, presented as a URL-encoded string.
 * @param unfurl_links Pass true to enable unfurling of primarily text-based content.
 * @param unfurl_media Pass false to disable unfurling of media content.
 * @param thread_ts Provide another message's `ts` value to make this message a reply. Avoid using a reply's `ts` value; use its parent instead.
 * @param reply_broadcast Used in conjunction with `thread_ts` and indicates whether reply should be made visible to everyone in the channel or conversation. Defaults to `false`.
 */
case class ScheduleMessageChatRequest(
  channel: String,
  post_at: Int,
  text: Option[String],
  parse: Option[String],
  as_user: Option[Boolean],
  link_names: Option[Boolean],
  attachments: Option[String],
  blocks: Option[String],
  unfurl_links: Option[Boolean],
  unfurl_media: Option[Boolean],
  thread_ts: Option[String],
  reply_broadcast: Option[Boolean]
)

object ScheduleMessageChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ScheduleMessageChatRequest] = deriveEncoder[ScheduleMessageChatRequest]
}

/**
 * @param channel The channel of the scheduled messages
 * @param latest A UNIX timestamp of the latest value in the time range
 * @param oldest A UNIX timestamp of the oldest value in the time range
 * @param limit Maximum number of original entries to return.
 * @param cursor For pagination purposes, this is the `cursor` value returned from a previous call to `chat.scheduledmessages.list` indicating where you want to start this call from.
 * @param team_id Required for org-wide apps
 */
case class ListScheduledMessagesChatRequest(
  channel: Option[String],
  latest: Option[String],
  oldest: Option[String],
  limit: Option[Int],
  cursor: Option[String],
  team_id: Option[String]
)

object ListScheduledMessagesChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListScheduledMessagesChatRequest] = deriveEncoder[ListScheduledMessagesChatRequest]
}

/**
 * @param channel Channel ID of the message
 * @param ts Timestamp of the message to add unfurl behavior to.
 * @param unfurls URL-encoded JSON map with keys set to URLs featured in the the message, pointing to their unfurl blocks or message attachments.
 * @param user_auth_message Provide a simply-formatted string to send as an ephemeral message to the user as invitation to authenticate further and enable full unfurling behavior
 * @param user_auth_required Set to `true` or `1` to indicate the user must install your Slack app to trigger unfurls for this domain
 * @param user_auth_url Send users to this custom URL where they will complete authentication in your app to fully trigger unfurling. Value should be properly URL-encoded.
 */
case class UnfurlChatRequest(
  channel: String,
  ts: String,
  unfurls: Option[String],
  user_auth_message: Option[String],
  user_auth_required: Option[Boolean],
  user_auth_url: Option[String]
)

object UnfurlChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[UnfurlChatRequest] = deriveEncoder[UnfurlChatRequest]
}

/**
 * @param channel Channel containing the message to be updated.
 * @param ts Timestamp of the message to be updated.
 * @param as_user Pass true to update the message as the authed user. [Bot users](/bot-users) in this context are considered authed users.
 * @param attachments A JSON-based array of structured attachments, presented as a URL-encoded string. This field is required when not presenting `text`. If you don't include this field, the message's previous `attachments` will be retained. To remove previous `attachments`, include an empty array for this field.
 * @param blocks A JSON-based array of [structured blocks](/block-kit/building), presented as a URL-encoded string. If you don't include this field, the message's previous `blocks` will be retained. To remove previous `blocks`, include an empty array for this field.
 * @param link_names Find and link channel names and usernames. Defaults to `none`. If you do not specify a value for this field, the original value set for the message will be overwritten with the default, `none`.
 * @param parse Change how messages are treated. Defaults to `client`, unlike `chat.postMessage`. Accepts either `none` or `full`. If you do not specify a value for this field, the original value set for the message will be overwritten with the default, `client`.
 * @param text New text for the message, using the [default formatting rules](/reference/surfaces/formatting). It's not required when presenting `blocks` or `attachments`.
 */
case class UpdateChatRequest(
  channel: String,
  ts: String,
  as_user: Option[Boolean],
  attachments: Option[String],
  blocks: Option[String],
  link_names: Option[String],
  parse: Option[String],
  text: Option[String]
)

object UpdateChatRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[UpdateChatRequest] = deriveEncoder[UpdateChatRequest]
}
