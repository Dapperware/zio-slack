package com.github.dapperware.slack.models.events

import com.github.dapperware.slack.models
import com.github.dapperware.slack.models.{ Attachment, Block, Channel, File, Im, ReactionItem, User }
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

// Marker trait for slack events we receive from the realtime system
sealed trait SlackEvent extends Serializable with Product

// TODO: Message Sub-types
case class Message(
  ts: String,
  channel: String,
  user: String,
  text: String,
  is_starred: Option[Boolean],
  thread_ts: Option[String]
) extends SlackEvent

case class EditMessage(user: Option[String], text: String, ts: String)

case class ReplyMarker(user: String, ts: String)

case class ReplyMessage(user: String, text: String, thread_ts: String, reply_count: Int, replies: Seq[ReplyMarker])

case class MessageChanged(
  message: EditMessage,
  previous_message: EditMessage,
  ts: String,
  event_ts: String,
  channel: String
) extends SlackEvent

case class MessageDeleted(ts: String, deleted_ts: String, event_ts: String, channel: String) extends SlackEvent

case class MessageReplied(ts: String, event_ts: String, channel: String, message: ReplyMessage) extends SlackEvent

case class BotMessage(
  ts: String,
  channel: String,
  text: String,
  bot_id: String,
  username: Option[String],
  attachments: Option[Seq[Attachment]]
) extends SlackEvent

// TODO: Message Sub-types
case class MessageWithSubtype(
  ts: String,
  channel: String,
  user: String,
  text: String,
  is_starred: Option[Boolean],
  messageSubType: MessageSubtype
) extends SlackEvent

sealed trait MessageSubtype {
  def subtype: String
}

object MessageSubtypes {

  // Fallback for unhandled message sub-types
  case class UnhandledSubtype(subtype: String) extends MessageSubtype

  case class MeMessage(subtype: String) extends MessageSubtype {
    //val subtype = "me_message"
  }

  case class ChannelNameMessage(oldname: String, name: String) extends MessageSubtype {
    val subtype = "channel_name"
  }

  case class FileShareMessage(file: File) extends MessageSubtype {
    val subtype = "file_share"
  }

}

case class AppMention(
  ts: String,
  channel: String,
  client_msg_id: String,
  text: Option[String],
  user: String,
  bot_id: Option[String],
  username: Option[String],
  blocks: Option[List[Block]],
  attachments: Option[List[Attachment]],
  event_ts: String
) extends SlackEvent

case class ReactionAdded(
  reaction: String,
  item: ReactionItem,
  event_ts: String,
  user: String,
  item_user: Option[String]
) extends SlackEvent

case class ReactionRemoved(
  reaction: String,
  item: ReactionItem,
  event_ts: String,
  user: String,
  item_user: Option[String]
) extends SlackEvent

case class UserTyping(channel: String, user: String) extends SlackEvent

case class ChannelMarked(channel: String, ts: String) extends SlackEvent

case class ChannelCreated(channel: Channel) extends SlackEvent

case class ChannelJoined(channel: Channel) extends SlackEvent

case class ChannelLeft(channel: String) extends SlackEvent

case class ChannelDeleted(channel: String) extends SlackEvent

case class ChannelRename(channel: Channel) extends SlackEvent

case class ChannelArchive(channel: String, user: String) extends SlackEvent

case class ChannelUnarchive(channel: String, user: String) extends SlackEvent

case class ChannelHistoryChanged(latest: Long, ts: String, event_ts: String) extends SlackEvent

case class ImCreated(user: String, channel: Im) extends SlackEvent

case class ImOpened(user: String, channel: String) extends SlackEvent

case class ImClose(user: String, channel: String) extends SlackEvent

case class ImMarked(channel: String, ts: String) extends SlackEvent

case class ImHistoryChanged(latest: Long, ts: String, event_ts: String) extends SlackEvent

case class GroupJoined(channel: Channel) extends SlackEvent

case class MpImJoined(channel: Channel) extends SlackEvent

case class MpImOpen(user: String, channel: String, event_ts: String) extends SlackEvent

case class MpImClose(user: String, channel: String, event_ts: String, converted_to: Option[String]) extends SlackEvent

case class GroupLeft(channel: String) extends SlackEvent

case class GroupOpen(user: String, channel: String) extends SlackEvent

case class GroupClose(user: String, channel: String) extends SlackEvent

case class GroupArchive(channel: String) extends SlackEvent

case class GroupUnarchive(channel: String) extends SlackEvent

case class GroupRename(channel: Channel) extends SlackEvent

case class GroupMarked(channel: String, ts: String) extends SlackEvent

case class GroupHistoryChanged(latest: Long, ts: String, event_ts: String) extends SlackEvent

case class FileCreated(file_id: String) extends SlackEvent

case class FileShared(file_id: String) extends SlackEvent

case class FileUnshared(file_id: String) extends SlackEvent

case class FilePublic(file_id: String) extends SlackEvent

case class FilePrivate(file: String) extends SlackEvent

case class FileChange(file_id: String) extends SlackEvent

case class FileDeleted(file_id: String, event_ts: String) extends SlackEvent

case class FileCommentAdded(
  file_id: String,
  comment: Json // TODO: SlackComment?
) extends SlackEvent

case class FileCommentEdited(
  file_id: String,
  comment: Json // TODO: SlackComment?
) extends SlackEvent

case class FileCommentDeleted(file_id: String, comment: String) extends SlackEvent

// Format of event is tbd
case class PinAdded(`type`: String) extends SlackEvent

// Format of event is tbd
case class PinRemoved(`type`: String) extends SlackEvent

case class PresenceChange(user: String, presence: String) extends SlackEvent

case class ManualPresenceChange(presence: String) extends SlackEvent

case class PrefChange(name: String, value: Json) extends SlackEvent

case class UserChange(user: User) extends SlackEvent

case class TeamJoin(user: User) extends SlackEvent

case class StarAdded(
  user: String,
  item: Json, // TODO: Different item types -- https://api.slack.com/methods/stars.list
  event_ts: String
) extends SlackEvent

case class StarRemoved(
  user: String,
  item: Json, // TODO: Different item types -- https://api.slack.com/methods/stars.list
  event_ts: String
) extends SlackEvent

case class EmojiChanged(event_ts: String) extends SlackEvent

case class CommandsChanged(event_ts: String) extends SlackEvent

case class TeamPlanChanged(plan: String) extends SlackEvent

case class TeamPrefChanged(
  name: String,
  value: String // TODO: Primitive type?
) extends SlackEvent

case class TeamRename(name: String) extends SlackEvent

case class TeamDomainChange(url: String, domain: String) extends SlackEvent

case class BotAdded(
  bot: Json // TODO: structure -- https://api.slack.com/events/bot_added
) extends SlackEvent

case class BotChanged(
  bot: Json // TODO: structure -- https://api.slack.com/events/bot_added
) extends SlackEvent

case class AccountsChanged(`type`: String) extends SlackEvent

case class TeamMigrationStarted(`type`: String) extends SlackEvent

case class ReconnectUrl(
  `type`: String,
  url: Option[String] // Optional because currently undocumented and could change
) extends SlackEvent

case class Reply(ok: Boolean, reply_to: Long, ts: String, text: String) extends SlackEvent

case class AppsChanged(app: models.App, event_ts: String) extends SlackEvent

case class AppsUninstalled(app_id: String, event_ts: String) extends SlackEvent

case class AppsInstalled(app: models.App, event_ts: String) extends SlackEvent

case class DesktopNotification(
  `type`: String,
  title: String,
  subtitle: String,
  msg: String,
  content: String,
  channel: String,
  launch_uri: String,
  avatar_image: String,
  ssb_filename: String,
  image_url: Option[String],
  is_shared: Boolean,
  event_ts: String
) extends SlackEvent

case class DndUpdatedUser(`type`: String, user: String, dnd_status: DndStatus, event_ts: String) extends SlackEvent

case class DndStatus(dnd_enabled: Boolean, next_dnd_start_ts: Long, next_dnd_end_ts: Long)

case class MemberJoined(user: String, channel: String, inviter: Option[String]) extends SlackEvent

case class MemberLeft(user: String, channel: String) extends SlackEvent

case class Pong(`type`: String, reply_to: Long) extends SlackEvent

case class MobileInAppNotification(
  `type`: String,
  title: String,
  subtitle: String,
  ts: String,
  channel: String,
  avatarImage: Option[String] = None,
  is_shared: Boolean = false,
  channel_name: String,
  author_id: String,
  author_display_name: Option[String] = None,
  msg_text: String,
  push_id: String,
  notif_id: String,
  mobileLaunchUri: Option[String],
  event_ts: String
) extends SlackEvent

//{"type":"mobile_in_app_notification","title":"HoneyMustard","subtitle":"#koi","ts":"1578232615.001200","channel":"CMX490U69","avatarImage":"https:\/\/secure.gravatar.com\/avatar\/4595af26f06d9e403b58657d7634455d.jpg?s=192&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0012-192.png","is_shared":false,"channel_name":"#koi","author_id":"U5KJZJSMA","author_display_name":"paulyd","msg_text":"@here howdy","push_id":"882356085971","notif_id":"98649cbca17a25c97ecfe789bb45220b","mobileLaunchUri":":\/\/channel?team=T5LBTFQMC&id=CMX490U69&ts=1578232615.001200","event_ts":"1578232616.000600"}

object SlackEvent {
  // Event Formats
  implicit val messageFmt: Codec.AsObject[Message]                              = deriveCodec[Message]
  implicit val messageRepl: Codec.AsObject[Reply]                               = deriveCodec[Reply]
  implicit val replyMarkerFmt: Codec.AsObject[ReplyMarker]                      = deriveCodec[ReplyMarker]
  implicit val editMessageFmt: Codec.AsObject[EditMessage]                      = deriveCodec[EditMessage]
  implicit val replyMessageFmt: Codec.AsObject[ReplyMessage]                    = deriveCodec[ReplyMessage]
  implicit val botMessageFmt: Codec.AsObject[BotMessage]                        = deriveCodec[BotMessage]
  implicit val messageChangedFmt: Codec.AsObject[MessageChanged]                = deriveCodec[MessageChanged]
  implicit val messageDeletedFmt: Codec.AsObject[MessageDeleted]                = deriveCodec[MessageDeleted]
  implicit val messageRepliedFmt: Codec.AsObject[MessageReplied]                = deriveCodec[MessageReplied]
  implicit val reactionAddedFmt: Codec.AsObject[ReactionAdded]                  = deriveCodec[ReactionAdded]
  implicit val reactionRemovedFmt: Codec.AsObject[ReactionRemoved]              = deriveCodec[ReactionRemoved]
  implicit val userTypingFmt: Codec.AsObject[UserTyping]                        = deriveCodec[UserTyping]
  implicit val channelMarkedFmt: Codec.AsObject[ChannelMarked]                  = deriveCodec[ChannelMarked]
  implicit val channelCreatedFmt: Codec.AsObject[ChannelCreated]                = deriveCodec[ChannelCreated]
  implicit val channelJoinedFmt: Codec.AsObject[ChannelJoined]                  = deriveCodec[ChannelJoined]
  implicit val channelLeftFmt: Codec.AsObject[ChannelLeft]                      = deriveCodec[ChannelLeft]
  implicit val channelDeletedFmt: Codec.AsObject[ChannelDeleted]                = deriveCodec[ChannelDeleted]
  implicit val channelRenameFmt: Codec.AsObject[ChannelRename]                  = deriveCodec[ChannelRename]
  implicit val channelArchiveFmt: Codec.AsObject[ChannelArchive]                = deriveCodec[ChannelArchive]
  implicit val channelUnarchiveFmt: Codec.AsObject[ChannelUnarchive]            = deriveCodec[ChannelUnarchive]
  implicit val channelHistoryChangedFmt: Codec.AsObject[ChannelHistoryChanged]  = deriveCodec[ChannelHistoryChanged]
  implicit val imCreatedFmt: Codec.AsObject[ImCreated]                          = deriveCodec[ImCreated]
  implicit val imOpenedFmt: Codec.AsObject[ImOpened]                            = deriveCodec[ImOpened]
  implicit val imCloseFmt: Codec.AsObject[ImClose]                              = deriveCodec[ImClose]
  implicit val imMarkedFmt: Codec.AsObject[ImMarked]                            = deriveCodec[ImMarked]
  implicit val imHistoryChangedFmt: Codec.AsObject[ImHistoryChanged]            = deriveCodec[ImHistoryChanged]
  implicit val mpImOpenFmt: Codec.AsObject[MpImOpen]                            = deriveCodec[MpImOpen]
  implicit val mpImCloseFmt: Codec.AsObject[MpImClose]                          = deriveCodec[MpImClose]
  implicit val mpImJoinFmt: Codec.AsObject[MpImJoined]                          = deriveCodec[MpImJoined]
  implicit val groupJoinFmt: Codec.AsObject[GroupJoined]                        = deriveCodec[GroupJoined]
  implicit val groupLeftFmt: Codec.AsObject[GroupLeft]                          = deriveCodec[GroupLeft]
  implicit val groupOpenFmt: Codec.AsObject[GroupOpen]                          = deriveCodec[GroupOpen]
  implicit val groupCloseFmt: Codec.AsObject[GroupClose]                        = deriveCodec[GroupClose]
  implicit val groupArchiveFmt: Codec.AsObject[GroupArchive]                    = deriveCodec[GroupArchive]
  implicit val groupUnarchiveFmt: Codec.AsObject[GroupUnarchive]                = deriveCodec[GroupUnarchive]
  implicit val groupRenameFmt: Codec.AsObject[GroupRename]                      = deriveCodec[GroupRename]
  implicit val groupMarkedFmt: Codec.AsObject[GroupMarked]                      = deriveCodec[GroupMarked]
  implicit val groupHistoryChangedFmt: Codec.AsObject[GroupHistoryChanged]      = deriveCodec[GroupHistoryChanged]
  implicit val fileCreatedFmt: Codec.AsObject[FileCreated]                      = deriveCodec[FileCreated]
  implicit val fileSharedFmt: Codec.AsObject[FileShared]                        = deriveCodec[FileShared]
  implicit val fileUnsharedFmt: Codec.AsObject[FileUnshared]                    = deriveCodec[FileUnshared]
  implicit val filePublicFmt: Codec.AsObject[FilePublic]                        = deriveCodec[FilePublic]
  implicit val filePrivateFmt: Codec.AsObject[FilePrivate]                      = deriveCodec[FilePrivate]
  implicit val fileChangeFmt: Codec.AsObject[FileChange]                        = deriveCodec[FileChange]
  implicit val fileDeletedFmt: Codec.AsObject[FileDeleted]                      = deriveCodec[FileDeleted]
  implicit val fileCommentAddedFmt: Codec.AsObject[FileCommentAdded]            = deriveCodec[FileCommentAdded]
  implicit val fileCommentEditedFmt: Codec.AsObject[FileCommentEdited]          = deriveCodec[FileCommentEdited]
  implicit val fileCommentDeletedFmt: Codec.AsObject[FileCommentDeleted]        = deriveCodec[FileCommentDeleted]
  implicit val pinAddedFmt: Codec.AsObject[PinAdded]                            = deriveCodec[PinAdded]
  implicit val pinRemovedFmt: Codec.AsObject[PinRemoved]                        = deriveCodec[PinRemoved]
  implicit val presenceChangeFmt: Codec.AsObject[PresenceChange]                = deriveCodec[PresenceChange]
  implicit val manualPresenceChangeFmt: Codec.AsObject[ManualPresenceChange]    = deriveCodec[ManualPresenceChange]
  implicit val prefChangeFmt: Codec.AsObject[PrefChange]                        = deriveCodec[PrefChange]
  implicit val userChangeFmt: Codec.AsObject[UserChange]                        = deriveCodec[UserChange]
  implicit val teamJoinFmt: Codec.AsObject[TeamJoin]                            = deriveCodec[TeamJoin]
  implicit val starAddedFmt: Codec.AsObject[StarAdded]                          = deriveCodec[StarAdded]
  implicit val starRemovedFmt: Codec.AsObject[StarRemoved]                      = deriveCodec[StarRemoved]
  implicit val emojiChangedFmt: Codec.AsObject[EmojiChanged]                    = deriveCodec[EmojiChanged]
  implicit val commandsChangedFmt: Codec.AsObject[CommandsChanged]              = deriveCodec[CommandsChanged]
  implicit val teamPlanChangedFmt: Codec.AsObject[TeamPlanChanged]              = deriveCodec[TeamPlanChanged]
  implicit val teamPrefChangedFmt: Codec.AsObject[TeamPrefChanged]              = deriveCodec[TeamPrefChanged]
  implicit val teamRenameFmt: Codec.AsObject[TeamRename]                        = deriveCodec[TeamRename]
  implicit val teamDomainChangeFmt: Codec.AsObject[TeamDomainChange]            = deriveCodec[TeamDomainChange]
  implicit val botAddedFmt: Codec.AsObject[BotAdded]                            = deriveCodec[BotAdded]
  implicit val botChangedFmt: Codec.AsObject[BotChanged]                        = deriveCodec[BotChanged]
  implicit val accountsChangedFmt: Codec.AsObject[AccountsChanged]              = deriveCodec[AccountsChanged]
  implicit val teamMigrationStartedFmt: Codec.AsObject[TeamMigrationStarted]    = deriveCodec[TeamMigrationStarted]
  implicit val reconnectUrlFmt: Codec.AsObject[ReconnectUrl]                    = deriveCodec[ReconnectUrl]
  implicit val appsChangedFmt: Codec.AsObject[AppsChanged]                      = deriveCodec[AppsChanged]
  implicit val appsUninstalledFmt: Codec.AsObject[AppsUninstalled]              = deriveCodec[AppsUninstalled]
  implicit val appsInstalledFmt: Codec.AsObject[AppsInstalled]                  = deriveCodec[AppsInstalled]
  implicit val desktopNotificationFmt: Codec.AsObject[DesktopNotification]      = deriveCodec[DesktopNotification]
  implicit val dndStatusFmt: Codec.AsObject[DndStatus]                          = deriveCodec[DndStatus]
  implicit val dndUpdateUserFmt: Codec.AsObject[DndUpdatedUser]                 = deriveCodec[DndUpdatedUser]
  implicit val memberJoined: Codec.AsObject[MemberJoined]                       = deriveCodec[MemberJoined]
  implicit val memberLeft: Codec.AsObject[MemberLeft]                           = deriveCodec[MemberLeft]
  implicit val pong: Codec.AsObject[Pong]                                       = deriveCodec[Pong]
  implicit val mobileInAppNotification: Codec.AsObject[MobileInAppNotification] = deriveCodec[MobileInAppNotification]
  implicit val appMentionCodec: Codec.AsObject[AppMention]                      = deriveCodec[AppMention]

  // Message sub-types
  import MessageSubtypes._

  implicit val messageSubtypeMeMessageFmt: Codec.AsObject[MeMessage]                   = deriveCodec[MeMessage]
  implicit val messageSubtypeChannelNameMessageFmt: Codec.AsObject[ChannelNameMessage] = deriveCodec[ChannelNameMessage]
  implicit val messageSubtypeFileShareMessageFmt: Codec.AsObject[FileShareMessage]     = deriveCodec[FileShareMessage]
  implicit val messageSubtypeHandledSubtypeFmt: Codec.AsObject[UnhandledSubtype]       = deriveCodec[UnhandledSubtype]

  implicit val messageWithSubtypeWrites: Encoder[MessageWithSubtype] = Encoder.forProduct6(
    "ts",
    "channel",
    "user",
    "text",
    "is_starred",
    "subtype"
  )((msg: MessageWithSubtype) => (msg.ts, msg.channel, msg.user, msg.text, msg.is_starred, msg.messageSubType.subtype))

  // Event Reads/Writes
  implicit val slackEventWrites: Encoder[SlackEvent] = Encoder.instance[SlackEvent] {
    case e: AppMention              => e.asJson
    case e: Message                 => e.asJson
    case e: Reply                   => e.asJson
    case e: MessageChanged          => e.asJson
    case e: MessageDeleted          => e.asJson
    case e: MessageReplied          => e.asJson
    case e: BotMessage              => e.asJson
    case e: MessageWithSubtype      => e.asJson
    case e: UserTyping              => e.asJson
    case e: ReactionAdded           => e.asJson
    case e: ReactionRemoved         => e.asJson
    case e: ChannelMarked           => e.asJson
    case e: ChannelCreated          => e.asJson
    case e: ChannelJoined           => e.asJson
    case e: ChannelLeft             => e.asJson
    case e: ChannelDeleted          => e.asJson
    case e: ChannelRename           => e.asJson
    case e: ChannelArchive          => e.asJson
    case e: ChannelUnarchive        => e.asJson
    case e: ChannelHistoryChanged   => e.asJson
    case e: ImCreated               => e.asJson
    case e: ImOpened                => e.asJson
    case e: ImClose                 => e.asJson
    case e: ImMarked                => e.asJson
    case e: ImHistoryChanged        => e.asJson
    case e: MpImOpen                => e.asJson
    case e: MpImClose               => e.asJson
    case e: MpImJoined              => e.asJson
    case e: GroupJoined             => e.asJson
    case e: GroupLeft               => e.asJson
    case e: GroupOpen               => e.asJson
    case e: GroupClose              => e.asJson
    case e: GroupArchive            => e.asJson
    case e: GroupUnarchive          => e.asJson
    case e: GroupRename             => e.asJson
    case e: GroupMarked             => e.asJson
    case e: GroupHistoryChanged     => e.asJson
    case e: FileCreated             => e.asJson
    case e: FileShared              => e.asJson
    case e: FileUnshared            => e.asJson
    case e: FilePublic              => e.asJson
    case e: FilePrivate             => e.asJson
    case e: FileChange              => e.asJson
    case e: FileDeleted             => e.asJson
    case e: FileCommentAdded        => e.asJson
    case e: FileCommentEdited       => e.asJson
    case e: FileCommentDeleted      => e.asJson
    case e: PinAdded                => e.asJson
    case e: PinRemoved              => e.asJson
    case e: PresenceChange          => e.asJson
    case e: ManualPresenceChange    => e.asJson
    case e: PrefChange              => e.asJson
    case e: UserChange              => e.asJson
    case e: TeamJoin                => e.asJson
    case e: StarAdded               => e.asJson
    case e: StarRemoved             => e.asJson
    case e: EmojiChanged            => e.asJson
    case e: CommandsChanged         => e.asJson
    case e: TeamPlanChanged         => e.asJson
    case e: TeamPrefChanged         => e.asJson
    case e: TeamRename              => e.asJson
    case e: TeamDomainChange        => e.asJson
    case e: BotAdded                => e.asJson
    case e: BotChanged              => e.asJson
    case e: AccountsChanged         => e.asJson
    case e: TeamMigrationStarted    => e.asJson
    case e: ReconnectUrl            => e.asJson
    case e: AppsChanged             => e.asJson
    case e: AppsUninstalled         => e.asJson
    case e: AppsInstalled           => e.asJson
    case e: DesktopNotification     => e.asJson
    case e: DndUpdatedUser          => e.asJson
    case e: MemberJoined            => e.asJson
    case e: MemberLeft              => e.asJson
    case e: Pong                    => e.asJson
    case e: MobileInAppNotification => e.asJson
  }

  implicit val subMessageReads: Decoder[MessageWithSubtype] = new Decoder[MessageWithSubtype] {

    override def apply(c: HCursor): Result[MessageWithSubtype] =
      for {
        subtype   <- c.downField("subtype").as[String]
        result    <- subtype match {
                       case "me_message"   => c.as[MeMessage]
                       case "channel_name" => c.as[ChannelNameMessage]
                       case "file_share"   => c.as[FileShareMessage]
                       case _              => c.as[UnhandledSubtype]
                     }
        ts        <- c.downField("ts").as[String]
        channel   <- c.downField("channel").as[String]
        user      <- c.downField("user").as[String]
        text      <- c.downField("text").as[String]
        isStarred <- c.downField("is_starred").as[Option[Boolean]]
      } yield MessageWithSubtype(ts, channel, user, text, isStarred, result)
  }

  implicit val slackEventReads: Decoder[SlackEvent] = new Decoder[SlackEvent] {

    override def apply(c: HCursor): Result[SlackEvent] = {
      val event: Either[DecodingFailure, SlackEvent] = for {
        etype   <- c.downField("type").as[String]
        subtype <- c.downField("subtype").as[Option[String]]
        result  <- etype match {
                     case "message" if subtype.contains("message_changed") => c.as[MessageChanged]
                     case "message" if subtype.contains("message_deleted") => c.as[MessageDeleted]
                     case "message" if subtype.contains("message_replied") => c.as[MessageReplied]
                     case "message" if subtype.contains("bot_message")     => c.as[BotMessage]
                     case "message" if subtype.isDefined                   => c.as[MessageWithSubtype]
                     case "message"                                        => c.as[Message]
                     case "app_mention"                                    => c.as[AppMention]
                     case "user_typing"                                    => c.as[UserTyping]
                     case "reaction_added"                                 => c.as[ReactionAdded]
                     case "reaction_removed"                               => c.as[ReactionRemoved]
                     case "channel_marked"                                 => c.as[ChannelMarked]
                     case "channel_created"                                => c.as[ChannelCreated]
                     case "channel_joined"                                 => c.as[ChannelJoined]
                     case "channel_left"                                   => c.as[ChannelLeft]
                     case "channel_deleted"                                => c.as[ChannelDeleted]
                     case "channel_rename"                                 => c.as[ChannelRename]
                     case "channel_archive"                                => c.as[ChannelArchive]
                     case "channel_unarchive"                              => c.as[ChannelUnarchive]
                     case "channel_history_changed"                        => c.as[ChannelHistoryChanged]
                     case "im_created"                                     => c.as[ImCreated]
                     case "im_open"                                        => c.as[ImOpened]
                     case "im_close"                                       => c.as[ImClose]
                     case "im_marked"                                      => c.as[ImMarked]
                     case "im_history_changed"                             => c.as[ImHistoryChanged]
                     case "mpim_open"                                      => c.as[MpImOpen]
                     case "mpim_close"                                     => c.as[MpImClose]
                     case "mpim_joined"                                    => c.as[MpImJoined]
                     case "group_joined"                                   => c.as[GroupJoined]
                     case "group_left"                                     => c.as[GroupLeft]
                     case "group_open"                                     => c.as[GroupOpen]
                     case "group_close"                                    => c.as[GroupClose]
                     case "group_archive"                                  => c.as[GroupArchive]
                     case "group_unarchive"                                => c.as[GroupUnarchive]
                     case "group_rename"                                   => c.as[GroupRename]
                     case "group_marked"                                   => c.as[GroupMarked]
                     case "group_history_changed"                          => c.as[GroupHistoryChanged]
                     case "file_created"                                   => c.as[FileCreated]
                     case "file_shared"                                    => c.as[FileShared]
                     case "file_unshared"                                  => c.as[FileUnshared]
                     case "file_public"                                    => c.as[FilePublic]
                     case "file_private"                                   => c.as[FilePrivate]
                     case "file_change"                                    => c.as[FileChange]
                     case "file_deleted"                                   => c.as[FileDeleted]
                     case "file_comment_added"                             => c.as[FileCommentAdded]
                     case "file_comment_edited"                            => c.as[FileCommentEdited]
                     case "file_comment_deleted"                           => c.as[FileCommentDeleted]
                     case "pin_added"                                      => c.as[PinAdded]
                     case "pin_removed"                                    => c.as[PinRemoved]
                     case "presence_change"                                => c.as[PresenceChange]
                     case "manual_presence_change"                         => c.as[ManualPresenceChange]
                     case "pref_change"                                    => c.as[PrefChange]
                     case "user_change"                                    => c.as[UserChange]
                     case "team_join"                                      => c.as[TeamJoin]
                     case "star_added"                                     => c.as[StarAdded]
                     case "star_removed"                                   => c.as[StarRemoved]
                     case "emoji_changed"                                  => c.as[EmojiChanged]
                     case "commands_changed"                               => c.as[CommandsChanged]
                     case "team_plan_changed"                              => c.as[TeamPlanChanged]
                     case "team_pref_changed"                              => c.as[TeamPrefChanged]
                     case "team_rename"                                    => c.as[TeamRename]
                     case "team_domain_change"                             => c.as[TeamDomainChange]
                     case "bot_added"                                      => c.as[BotAdded]
                     case "bot_changed"                                    => c.as[BotChanged]
                     case "accounts_changed"                               => c.as[AccountsChanged]
                     case "team_migration_started"                         => c.as[TeamMigrationStarted]
                     case "reconnect_url"                                  => c.as[ReconnectUrl]
                     case "apps_changed"                                   => c.as[AppsChanged]
                     case "apps_uninstalled"                               => c.as[AppsUninstalled]
                     case "apps_installed"                                 => c.as[AppsInstalled]
                     case "desktop_notification"                           => c.as[DesktopNotification]
                     case "dnd_updated_user"                               => c.as[DndUpdatedUser]
                     case "member_joined_channel"                          => c.as[MemberJoined]
                     case "member_left_channel"                            => c.as[MemberLeft]
                     case "pong"                                           => c.as[Pong]
                     case "mobile_in_app_notification"                     => c.as[MobileInAppNotification]
                     case t: String                                        => Left(DecodingFailure(s"Invalid type property: $t", List.empty))
                   }
      } yield result

      event.left.flatMap { failure =>
        (for {
          _      <- c.downField("reply_to").as[Long]
          result <- c.as[Reply]
        } yield result).left.map(_ => failure)
      }
    }
  }
}
