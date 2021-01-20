import zio.config._
import ConfigDescriptor._
import com.dapperware.slack.access.AccessToken
import zio.config.typesafe.TypesafeConfig
import zio.{FiberRef, Has, Layer, ZLayer}

package object common {
  type Basic = Has[BasicConfig]

  val default: Layer[ReadError[String], ZConfig[BasicConfig]] =
    TypesafeConfig.fromDefaultLoader(nested("basic") { BasicConfig.descriptor })

  object accessToken {
    val live: ZLayer[Basic, Nothing, AccessToken] =
      ZLayer.fromServiceM[BasicConfig, Any, Nothing, FiberRef[AccessToken.Token]](c => AccessToken.make(c.token))
  }

}
