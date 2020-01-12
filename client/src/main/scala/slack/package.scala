import io.circe
import io.circe.Json
import slack.core.{ AccessToken, SlackClient }
import slack.core.SlackClient.RequestEntity
import sttp.client._
import sttp.client.circe._
import zio.{ UIO, URIO, ZIO }

package object slack extends SlackEnvDefinition with SlackExtractors with SlackRequests {
  type SlackError = Throwable
}
