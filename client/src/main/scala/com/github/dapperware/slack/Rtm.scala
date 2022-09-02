package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedRtm
import com.github.dapperware.slack.generated.requests.ConnectRtmRequest
import com.github.dapperware.slack.generated.responses.ConnectRtmResponse
import zio.{ Trace, URIO, ZIO }

trait Rtm { self: SlackApiBase =>

  def connectRtm(
    batchPresenceAware: Option[Boolean] = None,
    presenceSub: Option[Boolean] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[ConnectRtmResponse]] =
    apiCall(Rtm.connectRtm(ConnectRtmRequest(batch_presence_aware = batchPresenceAware, presence_sub = presenceSub)))

}

private[slack] trait RtmAccessors { self: Slack.type =>

  def connectRtm(
    batchPresenceAware: Option[Boolean] = None,
    presenceSub: Option[Boolean] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[ConnectRtmResponse]] =
    ZIO.serviceWithZIO[Slack](_.connectRtm(batchPresenceAware, presenceSub))

}

object Rtm extends GeneratedRtm
