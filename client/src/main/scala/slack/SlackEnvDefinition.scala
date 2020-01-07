package slack

trait SlackEnvDefinition {
  type SlackEnv = SlackClient with AccessToken
}
