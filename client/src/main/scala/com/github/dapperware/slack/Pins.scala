package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedPins
import com.github.dapperware.slack.generated.requests.{AddPinsRequest, ListPinsRequest, RemovePinsRequest}
import com.github.dapperware.slack.generated.responses.ListPinsResponse
import zio.{Trace, URIO, ZIO}

trait Pins { self: SlackApiBase =>
  def pin(
    channelId: String,
    timeStamp: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Pins.addPins(AddPinsRequest(channelId, timeStamp)))

  def removePin(
    channelId: String,
    timeStamp: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(Pins.removePins(RemovePinsRequest(channelId, timeStamp)))

  // TODO This is wrong response type
  def listPins(
    channelId: String
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[ListPinsResponse]] =
    apiCall(Pins.listPins(ListPinsRequest(channelId)))
}

trait PinsAccessors { self: Slack.type =>
  def pin(
    channelId: String,
    timeStamp: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.pin(channelId, timeStamp))

  def removePin(
    channelId: String,
    timeStamp: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.removePin(channelId, timeStamp))

  // TODO This is wrong response type
  def listPins(
    channelId: String
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[ListPinsResponse]] =
    ZIO.serviceWithZIO[Slack](_.listPins(channelId))
}

object Pins extends GeneratedPins
