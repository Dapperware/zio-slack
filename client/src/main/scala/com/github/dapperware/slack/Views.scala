package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.EnrichedUnauthenticatedRequest
import com.github.dapperware.slack.models.{ View, ViewPayload }
import io.circe.Json
import io.circe.syntax._
import zio.{ Has, URIO }

trait Views {

  def openView(triggerId: String, view: ViewPayload): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request
      .make("views.open")
      .jsonBody(
        Json.obj(
          "trigger_id" -> triggerId.asJson,
          "view"       -> view.asJson
        )
      )
      .toCall

  def publishView(
    userId: String,
    view: ViewPayload,
    hash: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    Request
      .make("views.publish")
      .jsonBody(
        Json.obj(
          "user_id" -> userId.asJson,
          "view"    -> view.asJson,
          "hash"    -> hash.asJson
        )
      )
      .toCall

  def pushView(
    triggerId: String,
    view: ViewPayload
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[ViewPayload]] =
    Request
      .make("views.push")
      .jsonBody(
        Json.obj(
          "trigger_id" -> triggerId.asJson,
          "view"       -> view.asJson
        )
      )
      .at[ViewPayload]("view")
      .toCall

  def updateView(
    view: ViewPayload,
    externalId: Option[String] = None,
    hash: Option[String] = None,
    viewId: Option[String]
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[View]] =
    Request
      .make("views.update")
      .jsonBody(
        Json.obj(
          "view"        -> view.asJson,
          "external_id" -> externalId.asJson,
          "hash"        -> hash.asJson,
          "view_id"     -> viewId.asJson
        )
      )
      .at[View]("view")
      .toCall
}
