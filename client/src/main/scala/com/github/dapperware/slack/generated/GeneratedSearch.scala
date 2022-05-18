/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedSearch {

  /**
   * Searches for messages matching a query.
   * @see https://api.slack.com/methods/search.messages
   */
  def messagesSearch(req: MessagesSearchRequest): Request[Unit, AccessToken] =
    request("search.messages").formBody(req).auth.accessToken

}
