package slack.models

case class Reminder(
  id: String,
  creator: String,
  user: String,
  text: String,
  recurring: Boolean,
  time: Option[Long],
  complete_ts: Option[Long]
)
