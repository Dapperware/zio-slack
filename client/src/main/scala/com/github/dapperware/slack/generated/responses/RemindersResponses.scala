/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.responses

case class InfoRemindersResponse(reminder: com.github.dapperware.slack.models.Reminder)

object InfoRemindersResponse {
  implicit val decoder: io.circe.Decoder[InfoRemindersResponse] =
    io.circe.generic.semiauto.deriveDecoder[InfoRemindersResponse]
}

case class ListRemindersResponse(reminders: List[String])

object ListRemindersResponse {
  implicit val decoder: io.circe.Decoder[ListRemindersResponse] =
    io.circe.generic.semiauto.deriveDecoder[ListRemindersResponse]
}
