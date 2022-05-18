/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.generated.requests._
import com.github.dapperware.slack.generated.responses._
import com.github.dapperware.slack.{ AccessToken, ClientSecret, Request, Slack }
import Slack.request

trait GeneratedApi {

  /**
   * Checks API calling code.
   * @see https://api.slack.com/methods/api.test
   */
  def testApi(req: TestApiRequest): Request[Unit, Unit] =
    request("api.test").jsonBody(req).auth.none

}
