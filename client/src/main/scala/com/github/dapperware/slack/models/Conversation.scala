package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

import java.time.Instant

/**
 * @example
 * {
 * "id": "C0EAQDV4Z",
 * "name": "endeavor",
 * "is_channel": true,
 * "is_group": false,
 * "is_im": false,
 * "created": 1504554479,
 * "creator": "U0123456",
 * "is_archived": false,
 * "is_general": false,
 * "unlinked": 0,
 * "name_normalized": "endeavor",
 * "is_shared": false,
 * "is_ext_shared": false,
 * "is_org_shared": false,
 * "pending_shared": [],
 * "is_pending_ext_shared": false,
 * "is_member": true,
 * "is_private": false,
 * "is_mpim": false,
 * "last_read": "0000000000.000000",
 * "latest": null,
 * "unread_count": 0,
 * "unread_count_display": 0,
 * "topic": {
 * "value": "",
 * "creator": "",
 * "last_set": 0
 * },
 * "purpose": {
 * "value": "",
 * "creator": "",
 * "last_set": 0
 * },
 * "previous_names": [],
 * "priority": 0
 * }
 */
case class Conversation(
  id: String,
  name: String,
  isChannel: Boolean,
  isGroup: Boolean,
  isIm: Boolean,
  created: Instant,
  creator: String,
  isArchived: Boolean,
  isGeneral: Boolean,
  unlinked: Int,
  nameNormalized: String,
  isReadOnly: Boolean,
  isShared: Boolean,
  isExtShared: Boolean,
  isOrgShared: Boolean,
  pendingShared: List[String],
  isPendingExtShared: Boolean,
  isMember: Boolean,
  isPrivate: Boolean,
  isMpim: Boolean,
  lastRead: Instant,
  topic: ChannelValue,
  purpose: ChannelValue,
  previousName: List[String],
  numMembers: Int,
  locale: String
)

object Conversation {
  implicit val codec: Codec[Conversation] = deriveCodec[Conversation]
}
