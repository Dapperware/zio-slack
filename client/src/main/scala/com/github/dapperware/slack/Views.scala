package com.github.dapperware.slack

import com.github.dapperware.slack.generated.GeneratedViews
import com.github.dapperware.slack.generated.requests.{
  OpenViewsRequest,
  PublishViewsRequest,
  PushViewsRequest,
  UpdateViewsRequest
}
import com.github.dapperware.slack.models.{ View, ViewPayload }
import io.circe.syntax._
import zio.{ Has, URIO }

trait Views {

  def openView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    Views
      .openViews(
        OpenViewsRequest(
          trigger_id = triggerId,
          view = view.asJson.noSpaces
        )
      )
      .map(_.view)
      .toCall

  def publishView(
    userId: String,
    view: ViewPayload,
    hash: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    Views
      .publishViews(
        PublishViewsRequest(
          user_id = userId,
          view = view.asJson.noSpaces,
          hash = hash
        )
      )
      .map(_.view)
      .toCall

  def pushView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    Views
      .pushViews(PushViewsRequest(trigger_id = triggerId, view = view.asJson.noSpaces))
      .map(_.view)
      .toCall

  def updateView(
    view: ViewPayload,
    externalId: Option[String] = None,
    hash: Option[String] = None,
    viewId: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    Views
      .updateViews(
        UpdateViewsRequest(
          view = Some(view.asJson.noSpaces),
          external_id = externalId,
          hash = hash,
          view_id = viewId
        )
      )
      .map(_.view)
      .toCall

}

object Views extends GeneratedViews
