import io.circe
import io.circe.Json
import slack.SlackClient.RequestEntity
import sttp.client._
import sttp.client.circe._
import zio.{ UIO, URIO, ZIO }

package object slack extends SlackExtractors {

  type SlackResponse[T] = Either[ResponseError[circe.Error], Json]
  type SlackError       = Throwable
  type SlackEnv         = SlackClient with AccessToken

  def requestJson(method: String, body: Json): UIO[Request[SlackResponse[Json], Nothing]] =
    UIO.succeed(
      basicRequest
        .post(uri"https://slack.com/api/$method")
        .body(body)
        .response(asJson[Json])
    )

  def request(method: String, params: (String, SlackParamMagnet)*): UIO[Request[SlackResponse[Json], Nothing]] =
    UIO.succeed(
      basicRequest
        .post(uri"https://slack.com/api/$method")
        .body(
          Seq(params.map(p => p._2.produce.map(p._1 -> _)): _*).flatten.toMap
        )
        .response(asJson[Json])
    )

  def requestEntity(method: String, params: (String, SlackParamMagnet)*)(
    body: RequestEntity
  ): UIO[Request[SlackResponse[Json], Nothing]] = UIO.effectTotal(
    body(basicRequest)
      .post(uri"https://slack.com/api/$method?$params")
      .response(asJson[Json])
  )

  def sendM[R, T](request: URIO[R, Request[SlackResponse[T], Nothing]]): ZIO[R with SlackEnv, Throwable, Json] =
    request >>= AccessToken.authenticateM >>= SlackClient.send[T]

}
