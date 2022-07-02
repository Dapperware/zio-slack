package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedRtm
import com.github.dapperware.slack.generated.requests.ConnectRtmRequest
import com.github.dapperware.slack.generated.responses.ConnectRtmResponse
import zio.{ Has, URIO }

trait Rtm {

  def connectRtm(
    batchPresenceAware: Option[Boolean] = None,
    presenceSub: Option[Boolean] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ConnectRtmResponse]] =
    Rtm.connectRtm(ConnectRtmRequest(batch_presence_aware = batchPresenceAware, presence_sub = presenceSub)).toCall

}

object Rtm extends GeneratedRtm
