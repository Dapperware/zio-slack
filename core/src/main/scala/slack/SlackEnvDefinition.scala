package slack

import zio.Has

trait SlackEnvDefinition {
  type SlackEnv = _root_.slack.client.SlackClient with Has[AccessToken]
}
