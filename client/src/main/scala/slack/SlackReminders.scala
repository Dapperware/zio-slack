package slack

trait SlackReminders {
  val slackReminders: SlackReminders.Service[Any]
}

object SlackReminders {
  trait Service[R] {}
}
