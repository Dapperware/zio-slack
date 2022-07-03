package common

import zio.config._

case class BasicConfig(botToken: String, userToken: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] =
    (ConfigDescriptor
      .string("botToken") zip
      ConfigDescriptor.string("userToken") zip
      ConfigDescriptor.string("channel")).to
}
