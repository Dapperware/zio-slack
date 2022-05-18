/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param reminder The ID of the reminder to be marked as complete
 */
case class CompleteRemindersRequest(reminder: Option[String] = None)

object CompleteRemindersRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[CompleteRemindersRequest] = deriveEncoder[CompleteRemindersRequest]
}

/**
 * @param reminder The ID of the reminder
 */
case class DeleteRemindersRequest(reminder: Option[String] = None)

object DeleteRemindersRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteRemindersRequest] = deriveEncoder[DeleteRemindersRequest]
}

/**
 * @param reminder The ID of the reminder
 */
case class InfoRemindersRequest(reminder: Option[String] = None)

object InfoRemindersRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoRemindersRequest] = FormEncoder.fromParams.contramap[InfoRemindersRequest] {
    req =>
      List("reminder" -> req.reminder)
  }
}
