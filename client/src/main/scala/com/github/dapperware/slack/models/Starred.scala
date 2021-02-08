package com.github.dapperware.slack.models

import cats.Applicative.ops.toAllApplicativeOps
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import zio.Chunk

sealed trait Starred

/**
 * @see https://api.slack.com/methods/stars.list
 */
object Starred {
  private val typeDecoder: Decoder[String] = Decoder.decodeString

  private implicit val starredMessageDecoder     = deriveDecoder[StarredMessage]
  private implicit val starredFileDecoder        = deriveDecoder[StarredFile]
  private implicit val starredFileCommentDecoder = deriveDecoder[StarredFileComment]
  private implicit val starredChannelDecoder     = deriveDecoder[StarredChannel]
  private implicit val starredGroupDecoder       = deriveDecoder[StarredGroup]

  implicit val decoder: Decoder[Starred] = typeDecoder.flatMap {
    case "message"        => Decoder[StarredMessage].widen[Starred]
    case "file"           => Decoder[StarredFile].widen[Starred]
    case "file_comment"   => Decoder[StarredFileComment].widen[Starred]
    case "channel" | "im" => Decoder[StarredChannel].widen[Starred]
    case "group"          => Decoder[StarredGroup].widen[Starred]
  }
}

case class StarredMessage(message: Message)                     extends Starred
case class StarredFile(file: SlackFile)                         extends Starred
case class StarredFileComment(comment: String, file: SlackFile) extends Starred
case class StarredChannel(channel: String)                      extends Starred
case class StarredGroup(group: String)                          extends Starred

case class StarResponse(items: Chunk[Starred], response_metadata: Option[ResponseMetadata] = None)

object StarResponse {
  implicit val decoder: Decoder[StarResponse] = deriveDecoder[StarResponse]
}
