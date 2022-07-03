package common

import zio.config._

case class BasicConfig(botToken: String, userToken: String, appToken: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] =
    (ConfigDescriptor
      .string("botToken") zip
      ConfigDescriptor.string("userToken") zip
      ConfigDescriptor.string("appToken") zip
      ConfigDescriptor.string("channel")).to
}
