package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ Has, URIO, ZIO }

trait WithAccess {

  def authenticateM[U[_], T](request: RequestT[U, T, Any]): URIO[AccessToken, RequestT[U, T, Any]] =
    ZIO.accessM[AccessToken](_.get.authenticateM(request))

  def withAccessTokenM[R <: Has[_], E](token: ZIO[R, E, String], dummy: Boolean = false): WithAccessPartiallyM[R, E] =
    withAccessTokenM(token.map(AccessToken.Token))

  def withAccessTokenM[R <: Has[_], E](token: ZIO[R, E, AccessToken.Token]): WithAccessPartiallyM[R, E] =
    new WithAccessPartiallyM[R, E](token)

  def withAccessToken(token: String): WithAccessPartially =
    new WithAccessPartially(token)

  val accessToken: URIO[AccessToken, String] = URIO.access(_.get.token)
}
