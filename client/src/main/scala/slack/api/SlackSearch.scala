package slack.api

import io.circe.Json
import slack.{ SlackEnv, SlackError }
import zio.ZIO

object SlackSearch {

  trait Service {
    // TODO: Return proper search results (not JsValue)
    def searchFiles(query: String,
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
          "sortDir"   -> sortDir,
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
          "search.files",
          "query"     -> query,
          "sort"      -> sort,
          "sortDir"   -> sortDir,
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

}

object search extends SlackSearch.Service
