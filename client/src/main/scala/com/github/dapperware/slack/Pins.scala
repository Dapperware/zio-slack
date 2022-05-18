package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.{ request, EnrichedAuthRequest }
import com.github.dapperware.slack.generated.GeneratedPins
import com.github.dapperware.slack.generated.requests.{ AddPinsRequest, ListPinsRequest, RemovePinsRequest }
import zio.{ Has, URIO }

trait Pins {
  def pin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Pins.addPins(AddPinsRequest(channelId, timeStamp)).toCall

  def removePin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Pins.removePins(RemovePinsRequest(channelId, timeStamp)).toCall

  // TODO This is wrong response type
  def listPins(
    channelId: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Pins.listPins(ListPinsRequest(channelId)).toCall
}

object Pins extends GeneratedPins
