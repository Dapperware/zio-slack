package com.github.dapperware.slack

import com.github.dapperware.slack.client.RequestEntity
import io.circe.Json
import sttp.client3.BasicRequestBody
import sttp.model.Part

sealed trait SlackBody

object SlackBody {
  val empty: SlackBody                                                                          = fromForm(Map.empty[String, String])
  def fromJson(json: Json): SlackBody                                                           = SlackBody.JsonBody(json)
  def fromForm(first: (String, SlackParamMagnet), rest: (String, SlackParamMagnet)*): SlackBody = fromForm(
    first +: rest.toList
  )
  def fromForm(form: List[(String, SlackParamMagnet)]): SlackBody                               =
    SlackBody.MixedBody(form.flatMap(p => p._2.produce.map(p._1 -> _)).toMap, None)
  def fromForm(form: Map[String, String]): SlackBody                                            = SlackBody.MixedBody(form)
  def fromMixed(form: Map[String, String], entity: RequestEntity): SlackBody                    = SlackBody.MixedBody(form, Some(entity))
  def fromParts(parts: List[Part[BasicRequestBody]]): SlackBody                                 = SlackBody.PartBody(parts)

  case class JsonBody(json: Json)                                                       extends SlackBody
  case class PartBody(parts: List[Part[BasicRequestBody]])                              extends SlackBody
  case class MixedBody(form: Map[String, String], entity: Option[RequestEntity] = None) extends SlackBody
}
