package slack

trait SlackUserGroups {
  val slackUserGroups: SlackUserGroups.Service[Any]
}

object SlackUserGroups {
  trait Service[R] {}
}
