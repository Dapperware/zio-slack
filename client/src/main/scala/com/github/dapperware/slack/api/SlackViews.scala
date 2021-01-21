package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import com.github.dapperware.slack.models.View
import io.circe.Json
import io.circe.syntax._
import zio.ZIO

trait SlackViews {

  def openView(triggerId: String, view: View): ZIO[SlackEnv, SlackError, View] =
    sendM(
      requestJson(
        "views.open",
        Json.obj(
          "trigger_id" -> triggerId.asJson,
          "view"       -> view.asJson
        )
      )
    ) >>= as[View]("view")

  def publishView(userId: String, view: View, hash: Option[String] = None): ZIO[SlackEnv, SlackError, View] =
    sendM(
      requestJson(
        "views.publish",
        Json.obj(
          "user_id" -> userId.asJson,
          "view"    -> view.asJson,
          "hash"    -> hash.asJson
        )
      )
    ) >>= as[View]("view")

  def pushView(triggerId: String, view: View): ZIO[SlackEnv, SlackError, View] =
    sendM(
      requestJson(
        "views.push",
        Json.obj(
          "trigger_id" -> triggerId.asJson,
          "view"       -> view.asJson
        )
      )
    ) >>= as[View]("view")

  def updateView(view: View,
                 externalId: Option[String] = None,
                 hash: Option[String] = None,
                 viewId: Option[String]): ZIO[SlackEnv, SlackError, View] =
    sendM(
      requestJson(
        "views.update",
        Json.obj(
          "view"        -> view.asJson,
          "external_id" -> externalId.asJson,
          "hash"        -> hash.asJson,
          "view_id"     -> viewId.asJson
        )
      )
    ) >>= as[View]("view")

}

object views extends SlackViews
