import zio.config._
import ConfigDescriptor._
import slack.AccessToken
import zio.config.typesafe.TypesafeConfig
import zio.{ Has, Layer, ZLayer }

package object common {
  type Basic = Has[BasicConfig]

  val default: Layer[ReadError[String], ZConfig[BasicConfig]] =
    TypesafeConfig.fromDefaultLoader(nested("basic") { BasicConfig.descriptor })

  object accessToken {
    val live: ZLayer[Basic, Nothing, Has[AccessToken]] = ZLayer.fromServiceM(c => AccessToken.make(c.token))
  }

}
