package com.github.dapperware.slack

import sttp.client3.{ Request, ResponseException }
import zio.{ Has, ZIO }

package object client {

  def send[T, E](request: Request[Either[ResponseException[String, E], T], Any]): ZIO[Has[SlackClient], Throwable, T] =
    ZIO.accessM(_.get.send(request))

}
