package com.github.dapperware.slack

import io.circe.Encoder
import SlackParamMagnet.StringParamMagnet

trait SlackParamLike[T] {
  def produce(t: T): SlackParamMagnet
}

object SlackParamLike extends LowPrioImplicitParamLike {

  implicit val stringParamLike: SlackParamLike[String] = new SlackParamLike[String] {
    override def produce(t: String): SlackParamMagnet = StringParamMagnet(Some(t))
  }

  implicit val intParamLike: SlackParamLike[Int] = new SlackParamLike[Int] {
    override def produce(t: Int): SlackParamMagnet = StringParamMagnet(Some(t.toString))
  }

  implicit val boolParamLike: SlackParamLike[Boolean] = new SlackParamLike[Boolean] {
    override def produce(t: Boolean): SlackParamMagnet = StringParamMagnet(Some(t.toString))
  }

  implicit def optionParamLike[T](implicit spl: SlackParamLike[T]): SlackParamLike[Option[T]] =
    new SlackParamLike[Option[T]] {
      override def produce(t: Option[T]): SlackParamMagnet =
        StringParamMagnet(t.flatMap(spl.produce(_).produce))
    }

}

trait LowPrioImplicitParamLike {
  implicit def jsonEncoderParamLike[A: Encoder]: SlackParamLike[A] = new SlackParamLike[A] {
    override def produce(a: A): SlackParamMagnet = StringParamMagnet(Some(Encoder[A].apply(a).noSpaces))
  }
}
