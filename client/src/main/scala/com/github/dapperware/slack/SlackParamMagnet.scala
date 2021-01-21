package com.github.dapperware.slack

import scala.language.implicitConversions

sealed trait SlackParamMagnet {
  private[slack] def produce: Option[String]
}

object SlackParamMagnet {

  private[slack] case class StringParamMagnet(produce: Option[String]) extends SlackParamMagnet

  implicit def fromParamLike[T: SlackParamLike](value: T): SlackParamMagnet =
    implicitly[SlackParamLike[T]].produce(value)

}
