package slack.api

import io.circe.Json
import slack.{ SlackEnv, SlackError }
import zio.ZIO
import zio.macros.annotation.mockable

@mockable
//@accessible
trait SlackStars {
  val slackStars: SlackStars.Service[Any]
}

object SlackStars {
  trait Service[R] {
    def listStars(userId: Option[String] = None,
                  count: Option[Int] = None,
                  page: Option[Int] = None): ZIO[R with SlackEnv, SlackError, Json] =
      sendM(request("stars.list", "user" -> userId, "count" -> count, "page" -> page))
  }
}

object stars extends SlackStars.Service[SlackEnv]
