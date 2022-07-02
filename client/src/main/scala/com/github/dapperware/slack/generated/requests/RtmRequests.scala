/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param batch_presence_aware Batch presence deliveries via subscription. Enabling changes the shape of `presence_change` events. See [batch presence](/docs/presence-and-status#batching).
 * @param presence_sub Only deliver presence events when requested by subscription. See [presence subscriptions](/docs/presence-and-status#subscriptions).
 */
case class ConnectRtmRequest(batch_presence_aware: Option[Boolean], presence_sub: Option[Boolean])

object ConnectRtmRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ConnectRtmRequest] = FormEncoder.fromParams.contramap[ConnectRtmRequest] { req =>
    List("batch_presence_aware" -> req.batch_presence_aware, "presence_sub" -> req.presence_sub)
  }
}
