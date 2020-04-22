package slack.core

import sttp.client.RequestT
import zio.{ Has, UIO, URIO, ZIO }

sealed trait SlackToken extends Serializable { self =>
  type SelfType <: Has[_]

  def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[SelfType, RequestT[U, T, S]]

}

object SlackToken {
  type Token = Has[SlackToken]
}

final case class AccessToken private (token: String) extends SlackToken {
  override type SelfType = Has[AccessToken]
  override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[SelfType, RequestT[U, T, S]] =
    UIO.succeed(request.auth.bearer(token))
}

object AccessToken {
  def make(token: String): UIO[AccessToken] = UIO.succeed(AccessToken(token))

  val token: URIO[Has[AccessToken], String] = URIO.access(_.get.token)
}

final case class ClientSecret private (clientId: String, clientSecret: String) extends SlackToken {
  override def authenticateM[U[_], T, S](request: RequestT[U, T, S]): URIO[SelfType, RequestT[U, T, S]] =
    UIO.succeed(request.auth.basic(clientId, clientSecret))

  override type SelfType = Has[ClientSecret]
}

object ClientSecret {
  def make(clientId: String, clientSecret: String): UIO[ClientSecret] =
    UIO.succeed(ClientSecret(clientId, clientSecret))

  val clientId: URIO[Has[ClientSecret], String]     = ZIO.access(_.get.clientId)
  val clientSecret: URIO[Has[ClientSecret], String] = ZIO.access(_.get.clientSecret)
}
