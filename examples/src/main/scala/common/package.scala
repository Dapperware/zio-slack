import zio.config._
import ConfigDescriptor._
import com.github.dapperware.slack.AccessToken
import zio.config.typesafe.TypesafeConfig
import zio.{ Has, Layer, ZIO }

package object common {

  val default: Layer[ReadError[String], Has[BasicConfig]] =
    TypesafeConfig.fromResourcePath(nested("basic")(BasicConfig.descriptor))

  val accessToken: ZIO[Has[BasicConfig], Nothing, AccessToken] =
    ZIO.service[BasicConfig].map(c => AccessToken(c.token))

}
