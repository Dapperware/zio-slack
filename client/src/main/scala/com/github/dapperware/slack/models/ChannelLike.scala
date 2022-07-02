package com.github.dapperware.slack.models

import io.circe.Decoder

sealed trait ChannelLike[T] {
  type ChannelType
  def isFull: Boolean
  def decoder(key: String): io.circe.Decoder[ChannelType]
}

case object ChannelLikeChannel extends ChannelLike[Channel] {
  override type ChannelType = Channel
  override def isFull: Boolean = true

  override def decoder(key: String): Decoder[Channel] =
    Decoder[Channel].at(key)
}
case object ChannelLikeId      extends ChannelLike[String]  {
  override type ChannelType = String
  override def isFull: Boolean = false

  override def decoder(key: String): Decoder[String] =
    Decoder[String].at(key)
}
