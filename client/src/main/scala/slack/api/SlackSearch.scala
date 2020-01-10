package slack.api

import io.circe.Json
import slack.{ request, sendM, SlackEnv, SlackError }
import zio.ZIO

//@mockable
//@accessible
trait SlackSearch {
  val slackSearch: SlackSearch.Service[Any]
}

object SlackSearch {

  trait Service[R] {
    // TODO: Return proper search results (not JsValue)
    def searchFiles(query: String,
                    sort: Option[String] = None,
                    sortDir: Option[String] = None,
                    highlight: Option[String] = None,
                    count: Option[Int] = None,
                    page: Option[Int] = None): ZIO[R with SlackEnv, SlackError, Json] =
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
                  page: Option[Int] = None): ZIO[R with SlackEnv, SlackError, Json] =
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
                       page: Option[Int] = None): ZIO[R with SlackEnv, SlackError, Json] =
      sendM(
        request(
          "search.messages",
          "query"     -> query,
          "sort"      -> sort,
          "sortDir"   -> sortDir,
          "highlight" -> highlight,
          "count"     -> count,
          "page"      -> page
        )
      )
  }

}

object search extends SlackSearch.Service[SlackEnv]
