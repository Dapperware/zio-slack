package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Pagination(first: Int, last: Int, page: Int, page_count: Int, per_page: Int, total_count: Int)

object Pagination {
  implicit val decoder: Decoder[Pagination] = deriveDecoder[Pagination]
}
