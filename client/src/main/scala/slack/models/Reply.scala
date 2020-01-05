package slack.models

import io.circe.Codec
import io.circe.generic.semiauto._

case class Reply(user: String, ts: String)

object Reply {
  implicit val codec: Codec[Reply] = deriveCodec[Reply]
}
