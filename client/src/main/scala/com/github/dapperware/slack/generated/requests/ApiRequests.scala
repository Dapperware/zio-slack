/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param error Error response to return
 * @param foo example property to return
 */
case class TestApiRequest(error: Option[String] = None, foo: Option[String] = None)

object TestApiRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[TestApiRequest] = deriveEncoder[TestApiRequest]
}
