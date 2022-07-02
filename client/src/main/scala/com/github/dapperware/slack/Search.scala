package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedSearch
import io.circe.Json
import zio.{ Has, URIO }

trait Search {

  // TODO: Return proper search results (not JsValue)
  def searchFiles(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Json]] =
    request("search.files")
      .formBody(
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
      .as[Json]
      .toCall

  // TODO: Return proper search results (not JsValue)
  def searchAll(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Json]] =
    request("search.all")
      .formBody(
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
      .as[Json]
      .toCall

  def searchMessages(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Json]] =
    request("search.messages")
      .formBody(
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
      .as[Json]
      .toCall
}

object Search extends GeneratedSearch
