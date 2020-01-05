package common

import zio.{ NeedsEnv, ZIO, ZManaged }

import scala.language.implicitConversions

/**
 * Pretend we have the next version of ZIO
 */
trait EnrichedManaged {

  implicit def managedOps[R, E, A](managed: ZManaged[R, E, A]): EnrichedManaged.Ops[R, E, A] =
    new EnrichedManaged.Ops[R, E, A](managed)

}

object EnrichedManaged {
  class Ops[-R, +E, +A](managed: ZManaged[R, E, A]) {
    def provideSomeM[R0, E1 >: E](r0: ZIO[R0, E1, R])(implicit ev: NeedsEnv[R]): ZManaged[R0, E1, A] =
      r0.toManaged_.flatMap(managed.provide)

    def provideSomeManaged[R0, E1 >: E](r0: ZManaged[R0, E1, R])(implicit ev: NeedsEnv[R]): ZManaged[R0, E1, A] =
      r0.flatMap(managed.provide)
  }
}
