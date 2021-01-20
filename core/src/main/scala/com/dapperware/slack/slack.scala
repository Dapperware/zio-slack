package com.dapperware

import com.dapperware.slack.access.AccessToken
import com.dapperware.slack.client.SlackClient

package object slack {
  type SlackError = Throwable
  type SlackEnv = SlackClient with AccessToken
}
