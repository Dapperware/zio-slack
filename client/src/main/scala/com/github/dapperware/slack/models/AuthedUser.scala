package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AuthedUser(
  id: String,
  scope: String,
  tokenType: String,    // "user"
  accessToken: String,  // xoxp-xxx-yyy
  refreshToken: String, // only when enabling token rotation
  expiresIn: Int        // in seconds; only when enabling token rotation
)

object AuthedUser {
  implicit val decoder: Decoder[AuthedUser] = deriveDecoder[AuthedUser]
}
