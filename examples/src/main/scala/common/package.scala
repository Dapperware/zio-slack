import zio.config._
import ConfigDescriptor._
import com.github.dapperware.slack.Token
import zio.config.typesafe.TypesafeConfig
import zio.{ Has, Layer, ZIO }

package object common {
  type Basic = Has[BasicConfig]

  val default: Layer[ReadError[String], Basic] =
    TypesafeConfig.fromDefaultLoader(nested("basic") { BasicConfig.descriptor })

  val accessToken: ZIO[Basic, Nothing, Token] =
    ZIO.service[BasicConfig].map(c => Token(c.token))

}
