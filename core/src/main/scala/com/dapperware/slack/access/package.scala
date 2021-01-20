package com.dapperware.slack

import sttp.client.RequestT
import zio.{ FiberRef, Has, UIO, URIO, ZIO }

package object access {
  type AccessToken  = Has[FiberRef[AccessToken.Token]]
  type ClientSecret = Has[ClientSecret.Token]

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
    ZIO.accessM[AccessToken](_.get.get.flatMap(_.authenticateM(request)))

  object secret {
    def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[ClientSecret, RequestT[U, T, S]] =
      ZIO.accessM[ClientSecret](_.get.authenticateM(request))
  }

  object AccessToken {

    final case class Token private (token: String) {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[AccessToken, RequestT[U, T, S]] =
        UIO.succeed(request.auth.bearer(token))
    }

    def make(token: String): UIO[FiberRef[AccessToken.Token]] = FiberRef.make(Token(token))

    val token: URIO[AccessToken, String] = URIO.accessM(_.get.get.map(_.token))
  }

  object ClientSecret {
    case class Token (clientId: String, clientSecret: String) {
      def authenticateM[U[_], T, S](request: RequestT[U, T, S]): UIO[RequestT[U, T, S]] =
        UIO.succeed(request.auth.basic(clientId, clientSecret))
    }

    def make(clientId: String, clientSecret: String): UIO[ClientSecret.Token] =
      UIO.succeed(ClientSecret.Token(clientId, clientSecret))

    val clientId: URIO[ClientSecret, String]     = ZIO.access(_.get.clientId)
    val clientSecret: URIO[ClientSecret, String] = ZIO.access(_.get.clientSecret)
  }
}
