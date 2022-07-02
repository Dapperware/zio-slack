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

trait Views { self: Slack =>

  def openView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[AccessToken], SlackResponse[View]] =
    apiCall(
      Views
        .openViews(
          OpenViewsRequest(
            trigger_id = triggerId,
            view = view.asJson.noSpaces
          )
        )
        .map(_.view)
    )

  def publishView(
    userId: String,
    view: ViewPayload,
    hash: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[View]] =
    apiCall(
      Views
        .publishViews(
          PublishViewsRequest(
            user_id = userId,
            view = view.asJson.noSpaces,
            hash = hash
          )
        )
        .map(_.view)
    )

  def pushView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[AccessToken], SlackResponse[View]] =
    apiCall(
      Views
        .pushViews(PushViewsRequest(trigger_id = triggerId, view = view.asJson.noSpaces))
        .map(_.view)
    )

  def updateView(
    view: ViewPayload,
    externalId: Option[String] = None,
    hash: Option[String] = None,
    viewId: Option[String]
  ): URIO[Has[AccessToken], SlackResponse[View]] =
    apiCall(
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
    )

}

private[slack] trait ViewsAccessors { _: Slack.type =>

  def openView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    URIO.accessM(_.get.openView(triggerId, view))

  def publishView(
    userId: String,
    view: ViewPayload,
    hash: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    URIO.accessM(_.get.publishView(userId, view, hash))

  def pushView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    URIO.accessM(_.get.pushView(triggerId, view))

  def updateView(
    view: ViewPayload,
    externalId: Option[String] = None,
    hash: Option[String] = None,
    viewId: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    URIO.accessM(_.get.updateView(view, externalId, hash, viewId))

}

object Views extends GeneratedViews
