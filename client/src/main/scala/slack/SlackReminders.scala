package slack

import zio.ZIO

trait SlackReminders {
  val slackReminders: SlackReminders.Service[Any]
}

object SlackReminders {
  trait Service[R] {

    // TODO the time constraint can use special "natural language formats"
    //  Should consider using the zio.duration or a custom rolled DSL
    def addReminder(text: String, time: String, user: String)

    def completeReminder(reminder: String)
    def deleteReminder(reminder: String): ZIO[R with SlackEnv, SlackError, Boolean]
    def getReminderInfo(reminder: String)
    def listReminders()

  }
}
