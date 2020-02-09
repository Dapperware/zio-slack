package slack.api

import io.circe.Json
import slack.{ SlackEnv, SlackError }
import zio.ZIO

//@mockable
//@accessible
trait SlackStars {
  val slackStars: SlackStars.Service
}

object SlackStars {
  trait Service {
    def listStars(userId: Option[String] = None,
                  count: Option[Int] = None,
                  page: Option[Int] = None): ZIO[SlackEnv, SlackError, Json] =
      sendM(request("stars.list", "user" -> userId, "count" -> count, "page" -> page))
  }
}

object stars extends SlackStars.Service
