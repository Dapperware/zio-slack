package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedSearch
import io.circe.Json
import zio.{ URIO, ZIO }

trait Search { self: Slack =>

  // TODO: Return proper search results (not JsValue)
  def searchFiles(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[AccessToken, SlackResponse[Json]] =
    apiCall(
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
    )

  // TODO: Return proper search results (not JsValue)
  def searchAll(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[AccessToken, SlackResponse[Json]] =
    apiCall(
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
    )

  def searchMessages(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[AccessToken, SlackResponse[Json]] =
    apiCall(
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
    )
}

private[slack] trait SearchAccessors { _: Slack.type =>

  // TODO: Return proper search results (not JsValue)
  def searchFiles(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Slack with AccessToken, SlackResponse[Json]] =
    ZIO.serviceWithZIO[Slack](_.searchFiles(query, sort, sortDir, highlight, count, page))

  // TODO: Return proper search results (not JsValue)
  def searchAll(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Slack with AccessToken, SlackResponse[Json]] =
    ZIO.serviceWithZIO[Slack](_.searchAll(query, sort, sortDir, highlight, count, page))

  def searchMessages(
    query: String,
    sort: Option[String] = None,
    sortDir: Option[String] = None,
    highlight: Option[String] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Slack with AccessToken, SlackResponse[Json]] =
    ZIO.serviceWithZIO[Slack](_.searchMessages(query, sort, sortDir, highlight, count, page))
}

object Search extends GeneratedSearch
