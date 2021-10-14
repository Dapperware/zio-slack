package common

import zio.config._

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] =
    ConfigDescriptor
      .string("token")
      .zip(ConfigDescriptor.string("channel"))(
        (app: (String, String)) => BasicConfig(app._1, app._2),
        b => Some((b.token, b.channel))
      )
}
