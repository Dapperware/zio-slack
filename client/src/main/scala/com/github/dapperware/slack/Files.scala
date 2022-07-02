package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.models.{ FileInfo, FilesResponse }
import zio.{ Has, URIO }

import java.io.File

trait Files { self: Slack =>
  def revokePublicURL(fileId: String): URIO[Has[AccessToken], SlackResponse[FilesResponse]] =
    apiCall(request[FilesResponse]("files.revokePublicURL", "file" -> fileId))

  def sharedPublicURL(fileId: String): URIO[Has[AccessToken], SlackResponse[FilesResponse]] =
    apiCall(request[FilesResponse]("files.sharedPublicURL", "file" -> fileId))

  def deleteFile(fileId: String): URIO[Has[AccessToken], SlackResponse[Unit]]               =
    apiCall(request("files.delete").formBody(Map("file" -> fileId)))

  def getFileInfo(
    fileId: String,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[AccessToken], SlackResponse[FileInfo]]                                        =
    apiCall(request("files.info").formBody("file" -> fileId, "count" -> count, "page" -> page).as[FileInfo])

  def listFiles(
    userId: Option[String] = None,
    tsFrom: Option[String] = None,
    tsTo: Option[String] = None,
    types: Option[Seq[String]] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[AccessToken], SlackResponse[FilesResponse]]                                   =
    apiCall(
      request("files.list")
        .formBody(
          "user"    -> userId,
          "ts_from" -> tsFrom,
          "ts_to"   -> tsTo,
          "types"   -> types.map(_.mkString(",")),
          "count"   -> count,
          "page"    -> page
        )
        .as[FilesResponse]
    )

  def uploadFile(
    content: Either[File, Array[Byte]],
    fileType: Option[String] = None,
    fileName: Option[String] = None,
    title: Option[String] = None,
    initialComment: Option[String] = None,
    channels: Option[Seq[String]] = None,
    threadTs: Option[String] = None
  ): URIO[Has[AccessToken], SlackResponse[models.File]] = {
    val entity = content match {
      case Right(bytes) => RequestEntity(bytes, fileName)
      case Left(file)   => RequestEntity(file)
    }
    uploadFileFromEntity(entity, fileType, fileName, title, initialComment, channels, threadTs)
  }

  private def uploadFileFromEntity(
    entity: RequestEntity,
    filetype: Option[String],
    filename: Option[String],
    title: Option[String],
    initialComment: Option[String],
    channels: Option[Seq[String]],
    thread_ts: Option[String]
  ) =
    apiCall(
      request("files.upload")
        .entityBody(
          entity
        )(
          "filetype"        -> filetype,
          "filename"        -> filename,
          "title"           -> title,
          "initial_comment" -> initialComment,
          "channels"        -> channels.map(_.mkString(",")),
          "thread_ts"       -> thread_ts
        )
        .at[models.File]("file")
    )

}

trait FilesAccessors { _: Slack.type =>

  def revokePublicURL(fileId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[FilesResponse]] =
    URIO.accessM(_.get.revokePublicURL(fileId))

  def sharedPublicURL(fileId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[FilesResponse]] =
    URIO.accessM(_.get.sharedPublicURL(fileId))

  def deleteFile(fileId: String): URIO[Has[Slack] with Has[AccessToken], SlackResponse[Unit]] =
    URIO.accessM(_.get.deleteFile(fileId))

  def getFileInfo(
    fileId: String,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[FileInfo]] =
    URIO.accessM(_.get.getFileInfo(fileId, count, page))

  def listFiles(
    userId: Option[String] = None,
    tsFrom: Option[String] = None,
    tsTo: Option[String] = None,
    types: Option[Seq[String]] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[FilesResponse]] =
    URIO.accessM(_.get.listFiles(userId, tsFrom, tsTo, types, count, page))

  def uploadFile(
    content: Either[File, Array[Byte]],
    fileType: Option[String] = None,
    fileName: Option[String] = None,
    title: Option[String] = None,
    initialComment: Option[String] = None,
    channels: Option[Seq[String]] = None,
    threadTs: Option[String] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[models.File]] =
    URIO.accessM(_.get.uploadFile(content, fileType, fileName, title, initialComment, channels, threadTs))

}
