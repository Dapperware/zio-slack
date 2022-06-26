package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedChat
import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.models.{ Attachment, Block }
import io.circe.syntax._
import zio.{ Has, URIO }

import java.time.Instant

trait Chats {

  def permalink(
    channelId: String,
    ts: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[GetPermalinkChatResponse]] =
    Chats.getPermalinkChat(GetPermalinkChatRequest(channelId, ts)).toCall

  def deleteChat(
    channelId: String,
    ts: String,
    asUser: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[DeleteChatResponse]] =
    Chats.deleteChat(DeleteChatRequest(channelId, ts, asUser)).toCall

  def postChatEphemeral(
    channelId: String,
    text: Option[String] = None,
    user: String,
    asUser: Option[Boolean] = None,
    parse: Option[String] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    linkNames: Option[Boolean] = None,
    iconUrl: Option[String] = None,
    iconEmoji: Option[String] = None,
    threadTs: Option[String] = None,
    username: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[PostEphemeralChatResponse]] =
    Chats
      .postEphemeralChat(
        PostEphemeralChatRequest(
          channel = channelId,
          text = text,
          user = user,
          as_user = asUser,
          parse = parse,
          attachments = attachments.map(_.asJson.deepDropNullValues.noSpaces),
          blocks = blocks.map(_.asJson.deepDropNullValues.noSpaces),
          link_names = linkNames,
          icon_url = iconUrl,
          icon_emoji = iconEmoji,
          thread_ts = threadTs,
          username = username
        )
      )
      .toCall

  def postChatMessage(
    channelId: String,
    text: Option[String] = None,
    username: Option[String] = None,
    asUser: Option[Boolean] = None,
    parse: Option[String] = None,
    linkNames: Option[Boolean] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    unfurlLinks: Option[Boolean] = None,
    unfurlMedia: Option[Boolean] = None,
    iconUrl: Option[String] = None,
    iconEmoji: Option[String] = None,
    threadTs: Option[String] = None,
    mrkdown: Option[Boolean] = None,
    replyBroadcast: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[PostMessageChatResponse]] =
    Chats
      .postMessageChat(
        PostMessageChatRequest(
          channel = channelId,
          text = text,
          username = username,
          as_user = asUser,
          parse = parse,
          link_names = linkNames,
          attachments = attachments.map(_.asJson.deepDropNullValues.noSpaces),
          blocks = blocks.map(_.asJson.deepDropNullValues.noSpaces),
          unfurl_links = unfurlLinks,
          unfurl_media = unfurlMedia,
          icon_url = iconUrl,
          icon_emoji = iconEmoji,
          thread_ts = threadTs,
          mrkdwn = mrkdown,
          reply_broadcast = replyBroadcast
        )
      )
      .toCall

  def updateChatMessage(
    channelId: String,
    ts: String,
    text: Option[String] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    parse: Option[String] = None,
    linkNames: Option[String] = None,
    asUser: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UpdateChatResponse]] =
    Chats
      .updateChat(
        UpdateChatRequest(
          channel = channelId,
          ts = ts,
          text = text,
          attachments = attachments.map(_.asJson.deepDropNullValues.noSpaces),
          blocks = blocks.map(_.asJson.deepDropNullValues.noSpaces),
          parse = parse,
          link_names = linkNames,
          as_user = asUser
        )
      )
      .toCall

  def scheduleMessage(
    channel: String,
    postAt: Instant,
    text: String,
    asUser: Option[Boolean] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    linkNames: Option[Boolean] = None,
    parse: Option[String] = None,
    replyBroadcast: Option[Boolean] = None,
    threadTs: Option[String] = None,
    unfurlLinks: Option[Boolean] = None,
    unfurlMedia: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ScheduleMessageChatResponse]] =
    Chats
      .scheduleMessageChat(
        ScheduleMessageChatRequest(
          channel = channel,
          post_at = postAt.getEpochSecond.toInt,
          text = Some(text),
          as_user = asUser,
          attachments = attachments.map(_.asJson.deepDropNullValues.noSpaces),
          blocks = blocks.map(_.asJson.deepDropNullValues.noSpaces),
          link_names = linkNames,
          parse = parse,
          reply_broadcast = replyBroadcast,
          thread_ts = threadTs,
          unfurl_links = unfurlLinks,
          unfurl_media = unfurlMedia
        )
      )
      .toCall

  def listScheduledMessages(
    channel: Option[String] = None,
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    oldest: Option[String] = None,
    latest: Option[String] = None,
    teamId: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListScheduledMessagesChatResponse]] =
    Chats
      .listScheduledMessagesChat(
        ListScheduledMessagesChatRequest(
          channel = channel,
          cursor = cursor,
          limit = limit,
          oldest = oldest,
          latest = latest,
          team_id = teamId
        )
      )
      .toCall

  def deleteScheduleMessage(
    channel: String,
    scheduledMessageId: String,
    asUser: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Chats
      .deleteScheduledMessageChat(
        DeleteScheduledMessageChatRequest(
          channel = channel,
          scheduled_message_id = scheduledMessageId,
          as_user = asUser
        )
      )
      .toCall

}

object Chats extends GeneratedChat
