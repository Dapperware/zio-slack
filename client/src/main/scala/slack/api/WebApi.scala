package slack.api

/**
 * A service that interacts with slack and provides various messaging capabilities
 */
trait WebApi
    extends SlackApps
    with SlackAuth
    with SlackChannels
    with SlackChats
    with SlackConversations
    with SlackDialogs
    with SlackDnd
    with SlackEmojis
    with SlackFiles
    with SlackGroups
    with SlackIms
    with SlackOAuth
    with SlackProfile
    with SlackReactions
    with SlackReminders
    with SlackSearch
    with SlackStars
    with SlackTeams
    with SlackUsers
    with SlackUserGroups
    with SlackViews
