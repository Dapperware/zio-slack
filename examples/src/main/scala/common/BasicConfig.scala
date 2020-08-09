package common

import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] = DeriveConfigDescriptor.descriptor[BasicConfig]
}
