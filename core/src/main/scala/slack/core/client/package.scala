package slack.core

import java.io.File

import sttp.client.{ SttpBackend, _ }
import zio.{ Has, Task, ZIO, ZLayer }

package object client {
  type SlackClient = Has[SlackClient.Service]

  object SlackClient {
    trait Service {
      def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T]
    }

    val any = ZLayer.requires[SlackClient]

    def live(backend: SttpBackend[Task, Nothing, NothingT]): ZLayer.NoDeps[Nothing, SlackClient] =
      ZLayer.succeed(new Service {
        override def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[Any, Throwable, T] =
          for {
            result <- backend.send(request)
            json   <- ZIO.fromEither(result.body)
          } yield json
      })
  }

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

  def send[T, E](request: Request[Either[ResponseError[E], T], Nothing]): ZIO[SlackClient, Throwable, T] =
    ZIO.accessM(_.get.send(request))

}
