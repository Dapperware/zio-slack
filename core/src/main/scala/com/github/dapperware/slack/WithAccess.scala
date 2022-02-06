package com.github.dapperware.slack

import sttp.client3.RequestT
import zio.{ Has, URIO, ZIO }

trait WithAccess {

  def authenticateM[U[_], T](request: RequestT[U, T, Any]): URIO[Has[AccessToken], RequestT[U, T, Any]] =
    AccessToken.authenticateM(request)

  def withAccessTokenM[R <: Has[_], E](token: ZIO[R, E, String], dummy: Boolean = false): WithAccessPartiallyM[R, E] =
    withAccessTokenM(token.map(AccessToken.apply))

  def withAccessTokenM[R <: Has[_], E](token: ZIO[R, E, AccessToken]): WithAccessPartiallyM[R, E] =
    new WithAccessPartiallyM[R, E](token)

  def withAccessToken(token: String): WithAccessPartially =
    new WithAccessPartially(token)

  val accessToken: URIO[Has[AccessToken], String] = URIO.access(_.get.token)
}
