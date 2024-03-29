package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.client.RequestEntity
import com.github.dapperware.slack.models.{ FileInfo, FilesResponse }
import zio.{ Trace, URIO, ZIO }

import java.io.File

trait Files { self: SlackApiBase =>
  def revokePublicURL(fileId: String)(implicit trace: Trace): URIO[AccessToken, SlackResponse[FilesResponse]] =
    apiCall(request[FilesResponse]("files.revokePublicURL", "file" -> fileId).auth.accessToken)

  def sharedPublicURL(fileId: String)(implicit trace: Trace): URIO[AccessToken, SlackResponse[FilesResponse]] =
    apiCall(request[FilesResponse]("files.sharedPublicURL", "file" -> fileId).auth.accessToken)

  def deleteFile(fileId: String)(implicit trace: Trace): URIO[AccessToken, SlackResponse[Unit]]               =
    apiCall(request("files.delete").formBody(Map("file" -> fileId)).auth.accessToken)

  def getFileInfo(
    fileId: String,
    count: Option[Int] = None,
    page: Option[Int] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[FileInfo]]                                        =
    apiCall(
      request("files.info").formBody("file" -> fileId, "count" -> count, "page" -> page).as[FileInfo].auth.accessToken
    )

  def listFiles(
    userId: Option[String] = None,
    tsFrom: Option[String] = None,
    tsTo: Option[String] = None,
    types: Option[Seq[String]] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[FilesResponse]] =
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
        .auth
        .accessToken
    )

  def uploadFile(
    content: Either[File, Array[Byte]],
    fileType: Option[String] = None,
    fileName: Option[String] = None,
    title: Option[String] = None,
    initialComment: Option[String] = None,
    channels: Option[Seq[String]] = None,
    threadTs: Option[String] = None
  )(implicit trace: Trace): URIO[AccessToken, SlackResponse[models.File]] = {
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
        .jsonAt[models.File]("file")
        .auth
        .accessToken
    )

}

trait FilesAccessors { self: Slack.type =>

  def revokePublicURL(fileId: String)(implicit
    trace: Trace
  ): URIO[Slack with AccessToken, SlackResponse[FilesResponse]] =
    ZIO.serviceWithZIO[Slack](_.revokePublicURL(fileId))

  def sharedPublicURL(fileId: String)(implicit
    trace: Trace
  ): URIO[Slack with AccessToken, SlackResponse[FilesResponse]] =
    ZIO.serviceWithZIO[Slack](_.sharedPublicURL(fileId))

  def deleteFile(fileId: String)(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[Unit]] =
    ZIO.serviceWithZIO[Slack](_.deleteFile(fileId))

  def getFileInfo(
    fileId: String,
    count: Option[Int] = None,
    page: Option[Int] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[FileInfo]] =
    ZIO.serviceWithZIO[Slack](_.getFileInfo(fileId, count, page))

  def listFiles(
    userId: Option[String] = None,
    tsFrom: Option[String] = None,
    tsTo: Option[String] = None,
    types: Option[Seq[String]] = None,
    count: Option[Int] = None,
    page: Option[Int] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[FilesResponse]] =
    ZIO.serviceWithZIO[Slack](_.listFiles(userId, tsFrom, tsTo, types, count, page))

  def uploadFile(
    content: Either[File, Array[Byte]],
    fileType: Option[String] = None,
    fileName: Option[String] = None,
    title: Option[String] = None,
    initialComment: Option[String] = None,
    channels: Option[Seq[String]] = None,
    threadTs: Option[String] = None
  )(implicit trace: Trace): URIO[Slack with AccessToken, SlackResponse[models.File]] =
    ZIO.serviceWithZIO[Slack](_.uploadFile(content, fileType, fileName, title, initialComment, channels, threadTs))

}
