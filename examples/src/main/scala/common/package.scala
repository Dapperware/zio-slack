import zio.config._
import ConfigDescriptor._
import com.github.dapperware.slack.{ AccessToken, AppToken }
import zio.config.typesafe.TypesafeConfig
import zio.{ Has, Layer, ZIO }

package object common {

  val default: Layer[ReadError[String], Has[BasicConfig]] =
    TypesafeConfig.fromResourcePath(nested("basic")(BasicConfig.descriptor))

  val botToken: ZIO[Has[BasicConfig], Nothing, AccessToken] =
    ZIO.service[BasicConfig].map(c => AccessToken(c.botToken))

  val appToken: ZIO[Has[BasicConfig], Nothing, AppToken] =
    ZIO.service[BasicConfig].map(c => AppToken(c.appToken))

  val userToken: ZIO[Has[BasicConfig], Nothing, AccessToken] =
    ZIO.service[BasicConfig].map(c => AccessToken(c.userToken))

}
