package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedPins
import com.github.dapperware.slack.generated.requests.{ AddPinsRequest, ListPinsRequest, RemovePinsRequest }
import com.github.dapperware.slack.generated.responses.ListPinsResponse
import zio.{ Has, URIO }

trait Pins { self: Slack =>
  def pin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Pins.addPins(AddPinsRequest(channelId, timeStamp)))

  def removePin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[Unit]] =
    apiCall(Pins.removePins(RemovePinsRequest(channelId, timeStamp)))

  // TODO This is wrong response type
  def listPins(
    channelId: String
  ): URIO[Has[AccessToken], SlackResponse[ListPinsResponse]] =
    apiCall(Pins.listPins(ListPinsRequest(channelId)))
}

trait PinsAccessors { _: Slack.type =>
  def pin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.pin(channelId, timeStamp))

  def removePin(
    channelId: String,
    timeStamp: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.removePin(channelId, timeStamp))

  // TODO This is wrong response type
  def listPins(
    channelId: String
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ListPinsResponse]] =
    URIO.accessM(_.get.listPins(channelId))
}

object Pins extends GeneratedPins
