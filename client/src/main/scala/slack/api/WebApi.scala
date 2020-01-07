package slack.api

import slack.{
  SlackFiles,
  SlackGroups,
  SlackIms,
  SlackReactions,
  SlackReminders,
  SlackSearch,
  SlackStars,
  SlackTeams,
  SlackUsers
}

/**
 * A service that interacts with slack and provides various messaging capabilities
 */
trait WebApi[R]
    extends SlackAuth.Service[R]
    with SlackChannels.Service[R]
    with SlackChats.Service[R]
    with SlackConversations.Service[R]
    with SlackDialogs.Service[R]
    with SlackDnd.Service[R]
    with SlackEmojis.Service[R]
    with SlackFiles.Service[R]
    with SlackGroups.Service[R]
    with SlackIms.Service[R]
    with SlackReactions.Service[R]
    with SlackReminders.Service[R]
    with SlackSearch.Service[R]
    with SlackStars.Service[R]
    with SlackTeams.Service[R]
    with SlackUsers.Service[R]
