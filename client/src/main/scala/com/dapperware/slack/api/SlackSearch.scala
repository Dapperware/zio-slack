package com.dapperware.slack.api

import com.dapperware.slack.{SlackEnv, SlackError}
import io.circe.Json
import zio.ZIO

trait SlackSearch {

  // TODO: Return proper search results (not JsValue)
  def searchFiles(query: String,
                  sort: Option[String] = None,
                  sortDir: Option[String] = None,
                  highlight: Option[String] = None,
                  count: Option[Int] = None,
                  page: Option[Int] = None): ZIO[SlackEnv, SlackError, Json] =
    sendM(
      request(
        "search.files",
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
    )

  // TODO: Return proper search results (not JsValue)
  def searchAll(query: String,
                sort: Option[String] = None,
                sortDir: Option[String] = None,
                highlight: Option[String] = None,
                count: Option[Int] = None,
                page: Option[Int] = None): ZIO[SlackEnv, SlackError, Json] =
    sendM(
      request(
        "search.all",
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
    )

  // TODO: Return proper search results (not JsValue)
  def searchMessages(query: String,
                     sort: Option[String] = None,
                     sortDir: Option[String] = None,
                     highlight: Option[String] = None,
                     count: Option[Int] = None,
                     page: Option[Int] = None): ZIO[SlackEnv, SlackError, Json] =
    sendM(
      request(
        "search.messages",
        "query"     -> query,
        "sort"      -> sort,
        "sort_dir"  -> sortDir,
        "highlight" -> highlight,
        "count"     -> count,
        "page"      -> page
      )
    )

}

object search extends SlackSearch
