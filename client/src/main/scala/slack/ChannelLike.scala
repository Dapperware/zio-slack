package slack

import io.circe.{DecodingFailure, Json}
import slack.models.Channel
import zio.IO

sealed trait ChannelLike[T] {
  type ChannelType
  def isFull: Boolean
  def extract(t: Json, key: String): IO[DecodingFailure, ChannelType]
}

case object ChannelLikeChannel extends ChannelLike[Channel] {
  override type ChannelType = Channel
  override def isFull: Boolean = true

  override def extract(t: Json, key: String): IO[DecodingFailure, Channel] =
    as[Channel](key)(t)

}
case object ChannelLikeId extends ChannelLike[String] {
  override type ChannelType = String
  override def isFull: Boolean = false

  override def extract(t: Json, key: String): IO[DecodingFailure, String] =
    as[String](key)(t)
}
