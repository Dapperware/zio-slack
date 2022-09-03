package com.github.dapperware.slack

import zio.{ Trace, URIO, ZIO, ZLayer }
import zio.test._

object AccessTokenSpec extends ZIOSpecDefault {
  def constantClient: SlackClient = new SlackClient {
    override def apiCall[T, A](
      request: Request[T, A]
    )(implicit ev: HasAuth[A], tag: zio.Tag[A], trace: Trace): URIO[A, SlackResponse[T]] =
      ZIO.succeed(SlackError.ApiError("bad error"))
  }

  val accessToken  = AccessToken("abc123")
  val clientSecret = ClientSecret("myClient", "mySecret")

  override def spec = suite("tokens")(
    suite("AccessToken")(
      test("Adds auth bearer token") {

        val req = Request.make(MethodName("test")).auth.accessToken.toRequest("https://api.slack.com", accessToken)
        assertTrue(req.header("Authorization").contains("Bearer abc123"))
      },
      test("environment elimination") {
        AccessToken
          .authenticateWith(accessToken) {
            SlackClient.apiCall(Request.make(MethodName("test")).auth.accessToken)
          } *> assertCompletesZIO
      }
    ),
    suite("ClientSecret")(
      test("Adds auth bearer token") {
        val req = Request.make(MethodName("test")).auth.clientSecret.toRequest("https://api.slack.com", clientSecret)
        assertTrue(req.header("Authorization").contains("Basic bXlDbGllbnQ6bXlTZWNyZXQ="))
      },
      test("environment elimination") {
        ClientSecret
          .authenticateWith(clientSecret) {
            SlackClient.apiCall(Request.make(MethodName("test")).auth.clientSecret)
          } *> assertCompletesZIO
      }
    ),
    suite("AppToken")(
      test("Adds auth bearer token") {
        val req =
          Request.make(MethodName("test")).auth.appToken.toRequest("https://api.slack.com", AppToken("abc123"))
        assertTrue(req.header("Authorization").contains("Bearer abc123"))
      },
      test("environment elimination") {
        AppToken
          .authenticateWith(AppToken("abc123")) {
            SlackClient.apiCall(Request.make(MethodName("test")).auth.appToken)
          } *> assertCompletesZIO
      }
    )
  ).provide(ZLayer.succeed(constantClient))
}
