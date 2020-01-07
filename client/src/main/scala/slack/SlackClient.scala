package slack

import java.io.File

import io.circe.Json
import slack.SlackParamMagnet.StringParamMagnet
import sttp.client.{ SttpBackend, _ }
import zio.{ Task, UIO, ZIO }

import scala.language.{ higherKinds, implicitConversions }

//@mockable
trait SlackClient {
  val slackClient: SlackClient.Service[Any]
}

object SlackClient {

  def send[T](request: Request[slack.SlackResponse[T], Nothing]): ZIO[SlackClient, Throwable, Json] =
    ZIO.accessM(_.slackClient.send(request))

  def make(backend: SttpBackend[Task, Nothing, NothingT]): UIO[SlackClient] =
    UIO.effectTotal(new SlackClient {
      implicit private val b: SttpBackend[Task, Nothing, NothingT] = backend
      override val slackClient: Service[Any] = new Service[Any] {
        override def send[T](request: Request[SlackResponse[T], Nothing]): ZIO[Any, Throwable, Json] =
          for {
            result <- request.send()
            json   <- ZIO.fromEither(result.body)
          } yield json
      }
    })

  sealed trait RequestEntity {
    private[slack] def apply[U[_], T, S](request: RequestT[U, T, S]): RequestT[U, T, S]
  }

  object RequestEntity {
    def apply(file: File): RequestEntity                                          = FileEntity(file)
    def apply(array: Array[Byte], fileName: Option[String] = None): RequestEntity = ByteArrayEntity(array, fileName)
  }

  private case class FileEntity(file: File) extends RequestEntity {
    override private[slack] def apply[U[_], T, S](request: RequestT[U, T, S]) =
      request.multipartBody(
        multipartFile("file", file)
      )
  }
  private case class ByteArrayEntity(array: Array[Byte], fileName: Option[String]) extends RequestEntity {
    override private[slack] def apply[U[_], T, S](request: RequestT[U, T, S]) =
      request.multipartBody(
        fileName.foldLeft(multipart("file", array))(_.fileName(_))
      )
  }

  trait Service[R] {
    def send[T](request: Request[slack.SlackResponse[T], Nothing]): ZIO[R, Throwable, Json]
  }
}

sealed trait SlackParamMagnet {
  private[slack] def produce: Option[String]
}

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

object SlackParamMagnet {
  private[slack] case class StringParamMagnet(produce: Option[String]) extends SlackParamMagnet

  implicit def fromParamLike[T: SlackParamLike](value: T): SlackParamMagnet =
    implicitly[SlackParamLike[T]].produce(value)

}
