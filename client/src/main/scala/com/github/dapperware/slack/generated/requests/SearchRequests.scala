/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param query Search query.
 * @param count Pass the number of results you want per "page". Maximum of `100`.
 * @param highlight Pass a value of `true` to enable query highlight markers (see below).
 * @param page undefined
 * @param sort Return matches sorted by either `score` or `timestamp`.
 * @param sort_dir Change sort direction to ascending (`asc`) or descending (`desc`).
 */
case class MessagesSearchRequest(
  query: String,
  count: Option[Int] = None,
  highlight: Option[Boolean] = None,
  page: Option[Int] = None,
  sort: Option[String] = None,
  sort_dir: Option[String] = None
)

object MessagesSearchRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[MessagesSearchRequest] = FormEncoder.fromParams.contramap[MessagesSearchRequest] {
    req =>
      List(
        "query"     -> req.query,
        "count"     -> req.count,
        "highlight" -> req.highlight,
        "page"      -> req.page,
        "sort"      -> req.sort,
        "sort_dir"  -> req.sort_dir
      )
  }
}
