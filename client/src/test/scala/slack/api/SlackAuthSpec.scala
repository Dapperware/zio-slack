package slack.api

import sttp.capabilities
import sttp.client3.{ Request, Response, SttpBackend }
import sttp.monad.MonadError
import zio.Task

case class HijackingBackend(delegate: SttpBackend[Task, Any]) extends SttpBackend[Task, Any] {

  override def close(): Task[Unit] = delegate.close()

  override def responseMonad: MonadError[Task] = delegate.responseMonad

  def send[T, R >: Any with capabilities.Effect[Task]](request: Request[T, R]): Task[Response[T]] =
    delegate.send(request)
}

//class SlackAuthSpec extends DefaultRunnableSpec {
//  val client: ZLayer[Any, Throwable, SttpClient] = AsyncHttpClientZioBackend.layer().map { client =>
//    Has(HijackingBackend(client.get[SttpBackend[Task, Nothing, WebSocketHandler]]))
//  }
//  val token: Layer[Nothing, ClientToken] = AccessToken.live("abc123")
//
//  val spec =
//    suite("Auth")(
//      testM("test OK") {
//        assertM(slackTest)(isTrue)
//      }.provideCustomLayerShared((client >>> SlackClient.live) ++ token),
//      testM("testAuth OK") {
//        assertM(slackTestAuth)(equalTo(AuthIdentity("", "", "", "", "")))
//      }.provideCustomLayerShared((client >>> SlackClient.live) ++ token)
//    )
//}
