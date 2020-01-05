package slack

import io.circe.{Codec, Json}
import io.circe.syntax._
import io.circe.generic.semiauto._
import slack.models.{Block, PlainTextObject}
import zio.ZIO

trait SlackViews {
  val slackViews: SlackViews.Service[Any]
}

object SlackViews {
  trait Service[R] {

    def openView(triggerId: String, view: View): ZIO[SlackEnv, SlackError, View] =
      sendM(
        requestJson(
          "views.open",
          Json.obj(
            "trigger_id" -> triggerId.asJson,
            "view" -> view.asJson
          )
        )
      ) >>= as[View]("view")

    def publishView(userId: String, view: View, hash: Option[String] = None): ZIO[SlackEnv, Throwable, View] =
      sendM(
        requestJson(
          "views.publish",
          Json.obj(
            "user_id" -> userId.asJson,
            "view" -> view.asJson,
            "hash" -> hash.asJson
          )
        )
      ) >>= as[View]("view")

    def pushView(triggerId: String, view: View): ZIO[SlackEnv, Throwable, View] =
      sendM(
        requestJson(
          "views.push",
          Json.obj(
            "trigger_id" -> triggerId.asJson,
            "view" -> view.asJson
          )
        )
      ) >>= as[View]("view")

    def updateView(view: View,
                   externalId: Option[String] = None,
                   hash: Option[String] = None,
                   viewId: Option[String]): ZIO[SlackEnv, Throwable, View] =
      sendM(
        requestJson(
          "views.update",
          Json.obj(
            "view" -> view.asJson,
            "external_id" -> externalId.asJson,
            "hash" -> hash.asJson,
            "view_id" -> viewId.asJson
          )
        )
      ) >>= as[View]("view")

  }
}

case class View(
  `type`: String,
  title: PlainTextObject,
  blocks: Seq[Block],
  close: Option[PlainTextObject] = None,
  submit: Option[PlainTextObject] = None,
  privateMetaData: Option[String] = None,
  callbackId: Option[String] = None,
  clearOnClose: Option[Boolean] = None,
  notifyOnClose: Option[Boolean] = None,
  externalId: Option[String] = None
)

object View {
  import slack.models.TextObject.plainTextFmt

  implicit val codec: Codec[View] = deriveCodec[View]
}
