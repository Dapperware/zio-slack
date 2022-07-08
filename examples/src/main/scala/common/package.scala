import zio.config._
import ConfigDescriptor._
import com.github.dapperware.slack.{ AccessToken, AppToken }
import zio.config.typesafe.TypesafeConfig
import zio.{ Layer, ZIO, ZLayer }

package object common {

  val default: Layer[ReadError[String], BasicConfig] =
    TypesafeConfig.fromResourcePath(nested("basic")(BasicConfig.descriptor))

  val botToken: ZLayer[BasicConfig, Nothing, AccessToken] =
    ZLayer(ZIO.serviceWith[BasicConfig](c => AccessToken(c.botToken)))

  val appToken: ZLayer[BasicConfig, Nothing, AppToken] =
    ZLayer(ZIO.serviceWith[BasicConfig](c => AppToken(c.appToken)))

  val userToken: ZLayer[BasicConfig, Nothing, AccessToken] =
    ZLayer(ZIO.serviceWith[BasicConfig](c => AccessToken(c.userToken)))

}
