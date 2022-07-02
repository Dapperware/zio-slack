package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedRtm
import com.github.dapperware.slack.generated.requests.ConnectRtmRequest
import com.github.dapperware.slack.generated.responses.ConnectRtmResponse
import zio.{ Has, URIO }

trait Rtm { self: Slack =>

  def connectRtm(
    batchPresenceAware: Option[Boolean] = None,
    presenceSub: Option[Boolean] = None
  ): URIO[Has[AccessToken], SlackResponse[ConnectRtmResponse]] =
    apiCall(Rtm.connectRtm(ConnectRtmRequest(batch_presence_aware = batchPresenceAware, presence_sub = presenceSub)))

}

private[slack] trait RtmAccessors { _: Slack.type =>

  def connectRtm(
    batchPresenceAware: Option[Boolean] = None,
    presenceSub: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ConnectRtmResponse]] =
    URIO.accessM(_.get.connectRtm(batchPresenceAware, presenceSub))

}

object Rtm extends GeneratedRtm
