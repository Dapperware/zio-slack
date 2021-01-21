import zio.config._
import ConfigDescriptor._
import com.dapperware.slack.access
import com.dapperware.slack.access.AccessToken
import zio.config.typesafe.TypesafeConfig
import zio.{FiberRef, Has, Layer, ZIO, ZLayer}

package object common {
  type Basic = Has[BasicConfig]

  val default: Layer[ReadError[String], ZConfig[BasicConfig]] =
    TypesafeConfig.fromDefaultLoader(nested("basic") { BasicConfig.descriptor })

  val accessToken: ZIO[Basic, Nothing, AccessToken.Token] =
    ZIO.service[BasicConfig].map(c => AccessToken.Token(c.token))

}
