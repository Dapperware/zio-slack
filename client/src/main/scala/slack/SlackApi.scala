package slack

/**
 * A service that interacts with slack and provides various messaging capabilities
 */
trait SlackApi
    extends SlackAuth
    with SlackChannels
    with SlackChats
    with SlackConversations
    with SlackDialogs
    with SlackDnd
    with SlackEmojis
    with SlackFiles
    with SlackGroups
    with SlackIms
    with SlackReactions
    with SlackSearch
    with SlackStars
    with SlackTeams
    with SlackUsers

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

object api {
  object web extends WebApi[SlackEnv]
}
