package com.dapperware.slack

import sttp.client.RequestT
import zio.{ Has, IO, UIO, URIO, ZIO }

package object access {
  type AccessToken  = Has[AccessToken.Token]
  type ClientSecret = Has[ClientSecret.Token]

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
    ZIO.accessM[AccessToken](_.get.authenticateM(request))

  object secret {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
      ZIO.accessM[ClientSecret](_.get.authenticateM(request))
  }

  object AccessToken {

    final case class Token private (token: String) {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
        UIO.succeed(request.auth.bearer(token))
    }

    def make(token: String): UIO[AccessToken.Token] = UIO.succeed(Token(token))
  }

  object ClientSecret {
    case class Token(clientId: String, clientSecret: String) {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
        UIO.succeed(request.auth.basic(clientId, clientSecret))
    }

    def make(clientId: String, clientSecret: String): UIO[ClientSecret.Token] =
      UIO.succeed(ClientSecret.Token(clientId, clientSecret))

  }

  def withAccessTokenM[E](token: IO[E, String], dummy: Boolean = false): WithAccessPartiallyM[E] =
    withAccessTokenM(token.map(AccessToken.Token))

  def withAccessTokenM[E](token: IO[E, AccessToken.Token]): WithAccessPartiallyM[E] =
    new WithAccessPartiallyM[E](token)

  def withAccessToken(token: String): WithAccessPartially =
    new WithAccessPartially(token)

  val accessToken: URIO[AccessToken, String]   = URIO.access(_.get.token)
  val clientId: URIO[ClientSecret, String]     = ZIO.access(_.get.clientId)
  val clientSecret: URIO[ClientSecret, String] = ZIO.access(_.get.clientSecret)
}
