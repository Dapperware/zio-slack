package joke

import zio.{ ManagedApp, ZManaged }

object JokeApp extends ManagedApp {

  override def run(args: List[String]): ZManaged[zio.ZEnv, Nothing, Int] = ZManaged.succeed(0)
}
