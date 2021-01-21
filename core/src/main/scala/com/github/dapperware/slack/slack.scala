package com.github.dapperware

import com.github.dapperware.slack.client.SlackClient
import sttp.client.RequestT
import zio.{ Has, URIO, ZIO }

package object slack extends WithAccess {
  type SlackError   = Throwable
  type AccessToken  = Has[AccessToken.Token]
  type ClientSecret = Has[ClientSecret.Token]
  type SlackEnv     = SlackClient with AccessToken

  object secret {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
      ZIO.accessM[ClientSecret](_.get.authenticateM(request))
  }

  val clientId: URIO[ClientSecret, String]     = ZIO.access(_.get.clientId)
  val clientSecret: URIO[ClientSecret, String] = ZIO.access(_.get.clientSecret)
}
