package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{Trace, URIO, ZIO}

trait WithAccess {

  def authenticateZIO[U[_], T](request: RequestT[U, T, Any])(implicit trace: Trace): URIO[AccessToken, RequestT[U, T, Any]] =
    AccessToken.authenticateZIO(request)

  def withAccessTokenZIO[R, E](token: ZIO[R, E, String], dummy: Boolean = false): WithAccessPartiallyZIO[R, E] =
    withAccessTokenZIO(token.map(AccessToken.apply))

  def withAccessTokenZIO[R, E](token: ZIO[R, E, AccessToken]): WithAccessPartiallyZIO[R, E] =
    new WithAccessPartiallyZIO[R, E](token)

  def withAccessToken(token: String): WithAccessPartially =
    new WithAccessPartially(token)

  val accessToken: URIO[AccessToken, String] = ZIO.serviceWith(_.token)
}
