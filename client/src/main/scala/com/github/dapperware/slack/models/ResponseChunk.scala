package com.github.dapperware.slack.models

import io.circe.Decoder
import zio.Chunk
import cats.syntax.apply._

case class ResponseChunk[+T](
  items: Chunk[T],
  has_more: Option[Boolean] = None,
  response_metadata: Option[ResponseMetadata] = None
)

object ResponseChunk {
  import io.circe.zio._

  private def chunk[T: Decoder: Plural] = Decoder[Chunk[T]].at(Plural[T].plural)
  private val hasMore                   = Decoder[Option[Boolean]].at("has_more")
  private val responseMetadata          = Decoder[Option[ResponseMetadata]].at("response_metadata")

  implicit def decoder[T: Decoder: Plural]: Decoder[ResponseChunk[T]] =
    (chunk[T], hasMore, responseMetadata).mapN(ResponseChunk.apply)
}

case class Plural[T] private (plural: String)

object Plural {
  def apply[T](implicit ev: Plural[T]): Plural[T] = ev

  def const[T](p: String): Plural[T] = new Plural[T](p)

  implicit val pluralMembers: Plural[User]     = const("members")
  implicit val pluralChannels: Plural[Channel] = const("channels")
  implicit val pluralMessages: Plural[Message] = const("messages")
}
