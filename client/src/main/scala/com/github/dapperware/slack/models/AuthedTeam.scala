package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AuthedTeam(
  id: String,
  name: String
)

object AuthedTeam {
  implicit val decoder: Decoder[AuthedTeam] = deriveDecoder[AuthedTeam]
}
