package com.github.dapperware.slack.models

import cats.implicits.toFunctorOps
import io.circe.Decoder

case class ChannelLike(channel: Either[String, Channel])

object ChannelLike {
  implicit val decoder: Decoder[ChannelLike] =
    (Decoder[Channel]
      .map(Right(_))
      .widen[Either[String, Channel]] or Decoder[String].map(Left(_)).widen[Either[String, Channel]])
      .map(ChannelLike(_))
}
