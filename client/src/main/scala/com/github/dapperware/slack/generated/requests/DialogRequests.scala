/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param dialog The dialog definition. This must be a JSON-encoded string.
 * @param trigger_id Exchange a trigger to post to the user.
 */
case class OpenDialogRequest(dialog: String, trigger_id: String)

object OpenDialogRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[OpenDialogRequest] = deriveEncoder[OpenDialogRequest]
}
