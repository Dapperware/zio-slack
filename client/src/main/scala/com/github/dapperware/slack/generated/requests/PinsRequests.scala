/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param channel Channel to pin the item in.
 * @param timestamp Timestamp of the message to pin.
 */
case class AddPinsRequest(channel: String, timestamp: Option[String] = None)

object AddPinsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[AddPinsRequest] = deriveEncoder[AddPinsRequest]
}

/**
 * @param channel Channel to get pinned items for.
 */
case class ListPinsRequest(channel: String)

object ListPinsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListPinsRequest] = FormEncoder.fromParams.contramap[ListPinsRequest] { req =>
    List("channel" -> req.channel)
  }
}

/**
 * @param channel Channel where the item is pinned to.
 * @param timestamp Timestamp of the message to un-pin.
 */
case class RemovePinsRequest(channel: String, timestamp: Option[String] = None)

object RemovePinsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[RemovePinsRequest] = deriveEncoder[RemovePinsRequest]
}
