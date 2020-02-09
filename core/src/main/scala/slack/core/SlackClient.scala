package slack.core

import java.io.File

import sttp.client.{ SttpBackend, _ }
import zio.{ Managed, Task, UIO, ZIO }

import scala.language.higherKinds

//@mockable
trait SlackClient {
  val slackClient: SlackClient.Service
}

object SlackClient {

  def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[SlackClient, Throwable, T] =
    ZIO.accessM(_.slackClient.send(request))

  def make(backend: SttpBackend[Task, Nothing, NothingT]): UIO[SlackClient] =
    UIO.effectTotal(new SlackClient {
      implicit private val b: SttpBackend[Task, Nothing, NothingT] = backend
      override val slackClient: Service = new Service {
        override def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T] =
          for {
            result <- request.send()
            json   <- ZIO.fromEither(result.body)
          } yield json
      }
    })

  def makeManaged(backend: SttpBackend[Task, Nothing, NothingT]): Managed[Nothing, SlackClient] =
    make(backend).toManaged_

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

  trait Service {
    def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T]
  }
}
