/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedEmoji {

  /**
   * Lists custom emoji for a team.
   * @see https://api.slack.com/methods/emoji.list
   */
  def listEmoji: Request[Unit, AccessToken] =
    request("emoji.list").auth.accessToken

}
