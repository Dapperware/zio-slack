package slack

import io.circe.Json
import slack.SlackParamMagnet.StringParamMagnet

trait SlackParamLike[T] {
  def produce(t: T): SlackParamMagnet
}

object SlackParamLike {

  implicit val stringParamLike: SlackParamLike[String] = new SlackParamLike[String] {
    override def produce(t: String): SlackParamMagnet = StringParamMagnet(Some(t))
  }

  implicit val intParamLike: SlackParamLike[Int] = new SlackParamLike[Int] {
    override def produce(t: Int): SlackParamMagnet = StringParamMagnet(Some(t.toString))
  }

  implicit val boolParamLike: SlackParamLike[Boolean] = new SlackParamLike[Boolean] {
    override def produce(t: Boolean): SlackParamMagnet = StringParamMagnet(Some(t.toString))
  }

  implicit val jsonParamLike: SlackParamLike[Json] = new SlackParamLike[Json] {
    override def produce(t: Json): SlackParamMagnet = StringParamMagnet(Some(t.noSpaces))
  }

  implicit def optionParamLike[T](implicit spl: SlackParamLike[T]): SlackParamLike[Option[T]] =
    new SlackParamLike[Option[T]] {
      override def produce(t: Option[T]): SlackParamMagnet =
        StringParamMagnet(t.flatMap(spl.produce(_).produce))
    }

}
