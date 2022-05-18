/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param test Setting this parameter to `1` triggers a _testing mode_ where the specified token will not actually be revoked.
 */
case class RevokeAuthRequest(test: Option[Boolean] = None)

object RevokeAuthRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RevokeAuthRequest] = FormEncoder.fromParams.contramap[RevokeAuthRequest] { req =>
    List("test" -> req.test)
  }
}
