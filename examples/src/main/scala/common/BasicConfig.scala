package common

import zio.config._

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] =
    ConfigDescriptor
      .string("token")
      .zip(ConfigDescriptor.string("channel"))
      .to
}
