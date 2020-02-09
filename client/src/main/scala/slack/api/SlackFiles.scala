package slack.api

import java.io.File

import slack.core.SlackClient.RequestEntity
import slack.models.{ FileInfo, FilesResponse, SlackFile }
import slack.{ SlackEnv, SlackError }
import zio.ZIO

//@accessible
//@mockable
trait SlackFiles {
  val slackFiles: SlackFiles.Service
}

object SlackFiles {
  trait Service {
    def deleteFile(fileId: String): ZIO[SlackEnv, SlackError, Boolean] =
      sendM(request("files.delete", "file" -> fileId)) >>= isOk

    def getFileInfo(fileId: String,
                    count: Option[Int] = None,
                    page: Option[Int] = None): ZIO[SlackEnv, SlackError, FileInfo] =
      sendM(request("files.info", "file" -> fileId, "count" -> count, "page" -> page)) >>= as[FileInfo]

    def listFiles(
      userId: Option[String] = None,
      tsFrom: Option[String] = None,
      tsTo: Option[String] = None,
      types: Option[Seq[String]] = None,
      count: Option[Int] = None,
      page: Option[Int] = None
    ): ZIO[SlackEnv, SlackError, FilesResponse] =
      sendM(
        request(
          "files.list",
          "user"    -> userId,
          "ts_from" -> tsFrom,
          "ts_to"   -> tsTo,
          "types"   -> types.map(_.mkString(",")),
          "count"   -> count,
          "page"    -> page
        )
      ) >>= as[FilesResponse]

    def uploadFile(
      content: Either[File, Array[Byte]],
      fileType: Option[String] = None,
      fileName: Option[String] = None,
      title: Option[String] = None,
      initialComment: Option[String] = None,
      channels: Option[Seq[String]] = None,
      threadTs: Option[String] = None
    ): ZIO[SlackEnv, SlackError, SlackFile] = {
      val entity = content match {
        case Right(bytes) => RequestEntity(bytes, fileName)
        case Left(file)   => RequestEntity(file)
      }
      uploadFileFromEntity(entity, fileType, fileName, title, initialComment, channels, threadTs)
    }

    private def uploadFileFromEntity(entity: RequestEntity,
                                     filetype: Option[String],
                                     filename: Option[String],
                                     title: Option[String],
                                     initialComment: Option[String],
                                     channels: Option[Seq[String]],
                                     thread_ts: Option[String]): ZIO[SlackEnv, SlackError, SlackFile] =
      sendM(
        requestEntity(
          "files.upload",
          "filetype"        -> filetype,
          "filename"        -> filename,
          "title"           -> title,
          "initial_comment" -> initialComment,
          "channels"        -> channels.map(_.mkString(",")),
          "thread_ts"       -> thread_ts
        )(entity)
      ) >>= as[SlackFile]("file")
  }
}

object files extends SlackFiles.Service
