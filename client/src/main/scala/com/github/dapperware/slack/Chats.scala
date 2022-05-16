package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.models.{ Attachment, Block, ResponseChunk, UpdateResponse }
import io.circe.Json
import io.circe.syntax._
import zio.{ Has, URIO }

import java.time.Instant

trait Chats {

  def permalink(channelId: String, ts: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[String]] =
    Request
      .make("chat.getPermalink")
      .formBody(
        "channel"    -> channelId,
        "message_ts" -> ts
      )
      .at[String]("permalink")
      .toCall

  def deleteChat(
    channelId: String,
    ts: String,
    asUser: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request
      .make("chat.delete")
      .formBody(
        "channel" -> channelId,
        "ts"      -> ts
      )
      .toCall

  def postChatEphemeral(
    channelId: String,
    text: String,
    user: String,
    asUser: Option[Boolean] = None,
    parse: Option[String] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    linkNames: Option[Boolean] = None
  ) =
    request("chat.postEphemeral")
      .jsonBody(
        Json
          .obj(
            "channel" -> Json.fromString(channelId),
            "text"    -> Json.fromString(text),
            "user"    -> Json.fromString(user)
          )
          .deepMerge(
            Json.fromFields(
              Seq(
                asUser.map("as_user"          -> Json.fromBoolean(_)),
                parse.map("parse"             -> Json.fromString(_)),
                attachments.map("attachments" -> _.asJson),
                blocks.map("blocks"           -> _.asJson)
              ).flatten
            )
          )
      )
      .at[String]("message_ts")
      .toCall

  def postChatMessage(
    channelId: String,
    text: String,
    username: Option[String] = None,
    asUser: Option[Boolean] = None,
    parse: Option[String] = None,
    linkNames: Option[String] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    unfurlLinks: Option[Boolean] = None,
    unfurlMedia: Option[Boolean] = None,
    iconUrl: Option[String] = None,
    iconEmoji: Option[String] = None,
    replaceOriginal: Option[Boolean] = None,
    deleteOriginal: Option[Boolean] = None,
    threadTs: Option[String] = None,
    replyBroadcast: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[String]] =
    request("chat.postMessage")
      .jsonBody(
        Json.obj(
          "channel"          -> channelId.asJson,
          "text"             -> text.asJson,
          "username"         -> username.asJson,
          "as_user"          -> asUser.asJson,
          "parse"            -> parse.asJson,
          "link_names"       -> linkNames.asJson,
          "attachments"      -> attachments.asJson,
          "blocks"           -> blocks.asJson,
          "unfurl_links"     -> unfurlLinks.asJson,
          "unfurl_media"     -> unfurlMedia.asJson,
          "icon_url"         -> iconUrl.asJson,
          "icon_emoji"       -> iconEmoji.asJson,
          "replace_original" -> replaceOriginal.asJson,
          "delete_original"  -> deleteOriginal.asJson,
          "thread_ts"        -> threadTs.asJson,
          "reply_broadcast"  -> replyBroadcast.asJson
        )
      )
      .at[String]("ts")
      .toCall

  def updateChatMessage(
    channelId: String,
    ts: String,
    text: String,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    parse: Option[String] = None,
    linkNames: Option[String] = None,
    asUser: Option[Boolean] = None,
    threadTs: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[UpdateResponse]] =
    request("chat.update")
      .jsonBody(
        Json.obj(
          "channel"     -> channelId.asJson,
          "ts"          -> ts.asJson,
          "text"        -> text.asJson,
          "attachments" -> attachments.asJson,
          "blocks"      -> blocks.asJson,
          "parse"       -> parse.asJson,
          "link_names"  -> linkNames.asJson,
          "as_user"     -> asUser.asJson,
          "thread_ts"   -> threadTs.asJson
        )
      )
      .as[UpdateResponse]
      .toCall

  def scheduleMessage(
    channel: String,
    postAt: Instant,
    text: String,
    asUser: Option[Boolean] = None,
    attachments: Option[Seq[Attachment]] = None,
    blocks: Option[Seq[Block]] = None,
    linkNames: Option[String] = None,
    metadata: Option[String] = None,
    parse: Option[String] = None,
    replyBroadcast: Option[Boolean] = None,
    threadTs: Option[String] = None,
    unfurlLinks: Option[Boolean] = None,
    unfurlMedia: Option[Boolean] = None
  ) =
    request("chat.scheduleMessage")
      .jsonBody(
        Json.obj(
          "channel"         -> channel.asJson,
          "post_at"         -> postAt.toEpochMilli.asJson,
          "text"            -> text.asJson,
          "as_user"         -> asUser.asJson,
          "attachments"     -> attachments.asJson,
          "blocks"          -> blocks.asJson,
          "link_names"      -> linkNames.asJson,
          "metadata"        -> metadata.asJson,
          "parse"           -> parse.asJson,
          "reply_broadcast" -> replyBroadcast.asJson,
          "thread_ts"       -> threadTs.asJson,
          "unfurl_links"    -> unfurlLinks.asJson,
          "unfurl_media"    -> unfurlMedia.asJson
        )
      )
      .as[ScheduleResponse]

  def listScheduledMessages(
    channel: Option[String] = None,
    cursor: Option[String] = None,
    limit: Option[Int] = None,
    oldest: Option[Instant] = None,
    latest: Option[Instant] = None,
    teamId: Option[String] = None
  ) = request("chat.scheduledMessages.list")
    .jsonBody(
      Json.obj(
        "channel" -> channel.asJson,
        "cursor"  -> cursor.asJson,
        "limit"   -> limit.asJson,
        "oldest"  -> oldest.map(_.toEpochMilli).asJson,
        "latest"  -> latest.map(_.toEpochMilli).asJson,
        "team"    -> teamId.asJson
      )
    )
    .as[ResponseChunk[ScheduledMessage]]

  def deleteScheduleMessage(
    channel: String,
    scheduledMessageId: String,
    asUser: Option[Boolean] = None
  ) = request("chat.scheduledMessages.delete")
    .jsonBody(
      Json.obj(
        "channel"              -> channel.asJson,
        "scheduled_message_id" -> scheduledMessageId.asJson,
        "as_user"              -> asUser.asJson
      )
    )

}
