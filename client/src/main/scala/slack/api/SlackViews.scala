package slack.api

import io.circe.Json
import io.circe.syntax._
import slack.models.View
import slack.{SlackEnv, SlackError}
import zio.ZIO

trait SlackViews {
  val slackViews: SlackViews.Service[Any]
}

object SlackViews {
  trait Service[R] {

    def openView(triggerId: String, view: View): ZIO[R with SlackEnv, SlackError, View] =
      sendM(
        requestJson(
          "views.open",
          Json.obj(
            "trigger_id" -> triggerId.asJson,
            "view"       -> view.asJson
          )
        )
      ) >>= as[View]("view")

    def publishView(userId: String, view: View, hash: Option[String] = None): ZIO[R with SlackEnv, Throwable, View] =
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

    def pushView(triggerId: String, view: View): ZIO[R with SlackEnv, Throwable, View] =
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
                   viewId: Option[String]): ZIO[R with SlackEnv, Throwable, View] =
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
}
