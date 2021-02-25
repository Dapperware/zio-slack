package com.github.dapperware.slack.client

import sttp.client3.{ multipart, multipartFile, RequestT }

import java.io.File

sealed trait RequestEntity {
  private[slack] def apply[U[_], T, S](request: RequestT[U, T, S]): RequestT[U, T, S]
}

object RequestEntity {
  def apply(file: File): RequestEntity                                          = FileEntity(file)
  def apply(array: Array[Byte], fileName: Option[String] = None): RequestEntity = ByteArrayEntity(array, fileName)
}

private case class FileEntity(file: File)                                        extends RequestEntity {
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
