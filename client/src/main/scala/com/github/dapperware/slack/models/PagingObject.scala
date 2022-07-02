package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class PagingObject(count: Int, total: Int, page: Int, pages: Int)

object PagingObject {
  implicit val decoder: Decoder[PagingObject] = deriveDecoder[PagingObject]
}
