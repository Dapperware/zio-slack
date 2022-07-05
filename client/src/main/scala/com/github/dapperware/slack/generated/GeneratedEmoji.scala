/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.{ AccessToken, Request }

trait GeneratedEmoji {

  /**
   * Lists custom emoji for a team.
   * @see https://api.slack.com/methods/emoji.list
   */
  def listEmoji: Request[Unit, AccessToken] =
    request("emoji.list").auth.accessToken

}
