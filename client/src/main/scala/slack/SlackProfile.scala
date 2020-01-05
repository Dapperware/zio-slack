package slack

trait SlackProfile {
  val slackProfile: SlackProfile.Service[Any]
}

object SlackProfile {
  trait Service[R] {}
}
