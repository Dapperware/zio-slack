package com.github.dapperware.slack.models

import io.circe.Decoder

import java.time.Instant

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
