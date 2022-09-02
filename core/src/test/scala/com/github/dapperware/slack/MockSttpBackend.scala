package com.github.dapperware.slack

import sttp.client3.impl.zio.RIOMonadAsyncError
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{ Response, SttpBackend }
import sttp.model.StatusCode
import sttp.monad.MonadError
import sttp.{ capabilities, client3 }
import zio.stm.TQueue
import zio.{ Layer, Ref, Task, URIO, ZEnvironment, ZIO, ZLayer }

trait MockSttpBackend {

  val sttpBackEndStubLayer: ZLayer[Any, Nothing, SttpStubbing with SttpBackend[Task, Any]] =
    SttpStubbing.layer

  def accessTokenLayer(accessToken: String): Layer[Nothing, AccessToken] = ZLayer(AccessToken.make(accessToken))

}

trait SttpStubbing {
  def stub: Ref[SttpBackendStub[Task, Any]]

}

object SttpStubbing {

  /**
   * Specify how the stub backend should respond to requests matching the given predicate.
   *
   * Note that the stubs are immutable, and each new specification that is added yields a new stub instance.
   */
  def whenRequestMatches(p: client3.Request[_, _] => Boolean): WhenRequest =
    new WhenRequest(p)

  /**
   * Specify how the stub backend should respond to any request (catch-all).
   *
   * Note that the stubs are immutable, and each new specification that is added yields a new stub instance.
   */
  def whenAnyRequest: WhenRequest = whenRequestMatches(_ => true)

  /**
   * Specify how the stub backend should respond to requests using the given partial function.
   *
   * Note that the stubs are immutable, and each new specification that is added yields a new stub instance.
   */
  def whenRequestMatchesPartial(
    partial: PartialFunction[client3.Request[_, _], Response[_]]
  ): ZIO[SttpStubbing, Nothing, Unit] =
    ZIO.serviceWithZIO[SttpStubbing](_.stub.update(_.whenRequestMatchesPartial(partial)))

  class WhenRequest(p: client3.Request[_, _] => Boolean) {
    def thenRespondOk(): URIO[SttpStubbing, Unit] =
      ZIO.serviceWithZIO[SttpStubbing](_.stub.update(_.whenRequestMatches(p).thenRespondWithCode(StatusCode.Ok, "OK")))

    def thenRespondNotFound(): ZIO[SttpStubbing, Nothing, Unit] =
      ZIO.serviceWithZIO[SttpStubbing](
        _.stub.update(_.whenRequestMatches(p).thenRespondWithCode(StatusCode.NotFound, "Not found"))
      )

    def thenRespondServerError(): ZIO[SttpStubbing, Nothing, Unit] =
      ZIO.serviceWithZIO[SttpStubbing](
        _.stub.update(
          _.whenRequestMatches(p).thenRespondWithCode(StatusCode.InternalServerError, "Internal server error")
        )
      )

    def thenRespondWithCode(status: StatusCode, msg: String = ""): ZIO[SttpStubbing, Nothing, Unit] =
      thenRespond(Response(msg, status, msg))

    def thenRespond[T](body: T): ZIO[SttpStubbing, Nothing, Unit] =
      thenRespond(Response[T](body, StatusCode.Ok, "OK"))

    def thenRespond[T](body: T, statusCode: StatusCode): ZIO[SttpStubbing, Nothing, Unit] =
      thenRespond(Response[T](body, statusCode))

    def thenRespond[T](resp: => Response[T]): ZIO[SttpStubbing, Nothing, Unit] =
      ZIO.serviceWithZIO[SttpStubbing](_.stub.update(_.whenRequestMatches(p).thenRespond(resp)))

    def thenRespondCyclic[T](bodies: T*): ZIO[SttpStubbing, Nothing, Unit] =
      thenRespondCyclicResponses(bodies.map(body => Response[T](body, StatusCode.Ok, "OK")): _*)

    def thenRespondCyclicResponses[T](responses: Response[T]*): ZIO[SttpStubbing, Nothing, Unit] = ZIO.suspendSucceed {
      val mat = responses.toList
      (for {
        buffer <- TQueue.bounded[Response[T]](mat.length).commit
        _      <- buffer.offerAll(mat).commit
        next    = buffer.take.tap(r => buffer.offer(r)).commit
        _      <- thenRespondZIO(next)
      } yield ())
    }

    def thenRespondZIO(resp: => Task[Response[_]]): ZIO[SttpStubbing, Nothing, Unit] = {
      val m: PartialFunction[client3.Request[_, _], Task[Response[_]]] = {
        case r if p(r) => resp
      }

      ZIO.serviceWithZIO[SttpStubbing](_.stub.update(_.whenRequestMatches(p).thenRespondF(m)))
    }

    def thenRespondZIO(resp: client3.Request[_, _] => Task[Response[_]]): ZIO[SttpStubbing, Nothing, Unit] = {
      val m: PartialFunction[client3.Request[_, _], Task[Response[_]]] = {
        case r if p(r) => resp(r)
      }
      ZIO.serviceWithZIO[SttpStubbing](_.stub.update(_.whenRequestMatches(p).thenRespondF(m)))
    }
  }

  def layer: ZLayer[Any, Nothing, SttpStubbing with SttpBackend[Task, Any]] = ZLayer.fromZIOEnvironment(
    Ref.make[SttpBackendStub[Task, Any]](SttpBackendStub(new RIOMonadAsyncError[Any])).map { stub0 =>
      val stubber = new SttpStubbing {
        override def stub: Ref[SttpBackendStub[Task, Any]] = stub0
      }

      val proxy: SttpBackend[Task, Any] = new SttpBackend[Task, Any] {
        override def send[T, R >: capabilities.Effect[Task]](request: client3.Request[T, R]): Task[Response[T]] =
          stub0.get.flatMap(_.send(request))

        override def close(): Task[Unit] = stubber.stub.get.flatMap(_.close())

        override val responseMonad: MonadError[Task] = new RIOMonadAsyncError[Any]
      }

      ZEnvironment[SttpStubbing, SttpBackend[Task, Any]](stubber, proxy)
    }
  )
}
