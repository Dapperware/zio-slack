package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AuthedEnterprise(
  id: String,
  name: String
)

object AuthedEnterprise {
  implicit val decoder: Decoder[AuthedEnterprise] = deriveDecoder[AuthedEnterprise]
}
