package slack

import slack.core.AccessToken
import slack.core.client.SlackClient
import zio.Has

trait SlackEnvDefinition {
  type SlackEnv = SlackClient with Has[AccessToken]
}
