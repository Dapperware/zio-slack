package slack

import slack.core.{ AccessToken, SlackClient }

trait SlackEnvDefinition {
  type SlackEnv = SlackClient with AccessToken
}
