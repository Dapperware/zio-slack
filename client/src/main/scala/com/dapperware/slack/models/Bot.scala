package com.dapperware.slack.models

import java.time.Instant

import io.circe.Decoder

case class Bot(
  id: String,
  deleted: Boolean,
  name: String,
  app_id: String,
  user_id: String,
  updated: Option[Instant] = None,
  icons: Map[String, String] = Map.empty
)

object Bot {
  implicit val decoder: Decoder[Bot] = io.circe.generic.semiauto.deriveDecoder[Bot]
}
