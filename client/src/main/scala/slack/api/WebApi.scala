package slack.api

/**
 * A service that interacts with slack and provides various messaging capabilities
 */
trait WebApi
    extends SlackAuth.Service
    with SlackChannels.Service
    with SlackChats.Service
    with SlackConversations.Service
    with SlackDialogs.Service
    with SlackDnd.Service
    with SlackEmojis.Service
    with SlackFiles.Service
    with SlackGroups.Service
    with SlackIms.Service
    with SlackOAuth.Service
    with SlackProfile.Service
    with SlackReactions.Service
    with SlackReminders.Service
    with SlackSearch.Service
    with SlackStars.Service
    with SlackTeams.Service
    with SlackUsers.Service
    with SlackUserGroups.Service
    with SlackViews.Service
