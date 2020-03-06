package slack

import slack.core.access.ClientToken
import slack.core.client.SlackClient

trait SlackEnvDefinition {
  type SlackEnv = SlackClient with ClientToken
}
