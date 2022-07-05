/* This file was automatically generated update at your own risk */
package com.github.dapperware.slack.generated

import com.github.dapperware.slack.Request
import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.requests._

trait GeneratedApi {

  /**
   * Checks API calling code.
   * @see https://api.slack.com/methods/api.test
   */
  def testApi(req: TestApiRequest): Request[Unit, Unit] =
    request("api.test").jsonBody(req).auth.none

}
