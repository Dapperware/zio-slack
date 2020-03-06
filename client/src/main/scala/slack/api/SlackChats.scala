package slack.api

import io.circe.Json
import io.circe.syntax._
import slack.models.{ Attachment, Block, UpdateResponse }
import slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackChats {
  trait Service {
    def deleteChat(channelId: String, ts: String, asUser: Option[Boolean] = None): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("chat.delete", "channel" -> channelId, "ts" -> ts, "as_user" -> asUser)) >>= isOk

    def postChatEphemeral(
      channelId: String,
      text: String,
      user: String,
      asUser: Option[Boolean] = None,
      parse: Option[String] = None,
      attachments: Option[Seq[Attachment]] = None,
      blocks: Option[Seq[Block]] = None,
      linkNames: Option[Boolean] = None
    ): ZIO[SlackEnv, SlackError, String] =
      sendM(
        requestJson(
          "chat.postEphemeral",
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
      ) >>= as[String]("message_ts")

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
    ): ZIO[SlackEnv, SlackError, String] =
      sendM(
        requestJson(
          "chat.postMessage",
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
      ) >>= as[String]("ts")

    def updateChatMessage(channelId: String,
                          ts: String,
                          text: String,
                          attachments: Option[Seq[Attachment]] = None,
                          blocks: Option[Seq[Block]] = None,
                          parse: Option[String] = None,
                          linkNames: Option[String] = None,
                          asUser: Option[Boolean] = None,
                          threadTs: Option[String] = None): ZIO[SlackEnv, SlackError, UpdateResponse] =
      sendM(
        requestJson(
          "chat.update",
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
      ) >>= as[UpdateResponse]
  }
}

object chats extends SlackChats.Service
