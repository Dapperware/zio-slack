package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.generated.GeneratedFiles
import com.github.dapperware.slack.generated.requests.{
  InfoRemoteFilesRequest,
  RemoveRemoteFilesRequest,
  ShareRemoteFilesRequest,
  UpdateRemoteFilesRequest
}
import com.github.dapperware.slack.models.File
import io.circe.Decoder
import sttp.client3.multipart
import zio.{ Chunk, URIO, ZIO }

trait RemoteFiles { self: Slack =>
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ): URIO[AccessToken, SlackResponse[File]] =
    apiCall(
      RemoteFiles
        .addRemoteFiles(
          AddRemoteFilesRequest(
            external_id = externalId,
            external_url = externalUrl,
            title = title,
            file_type = fileType,
            indexable_file_contents = indexableFileContents,
            preview_image = previewImage
          )
        )
        .map(_.file)
    )

  def removeRemoteFile(
    fileId: Option[String] = None,
    externalId: Option[String] = None
  ): URIO[AccessToken, SlackResponse[Unit]] =
    apiCall(
      RemoteFiles
        .removeRemoteFiles(
          RemoveRemoteFilesRequest(
            file = fileId,
            external_id = externalId
          )
        )
    )

  def shareRemoteFile(
    channels: List[String],
    fileId: Option[String] = None,
    externalId: Option[String] = None
  ): URIO[AccessToken, SlackResponse[File]] =
    apiCall(
      RemoteFiles
        .shareRemoteFiles(
          ShareRemoteFilesRequest(file = fileId, external_id = externalId, channels = Some(channels.mkString(",")))
        )
        .map(_.file)
    )

  def getRemoteFile(
    fileId: Option[String],
    externalId: Option[String] = None
  ): URIO[AccessToken, SlackResponse[File]] =
    apiCall(
      RemoteFiles
        .infoRemoteFiles(
          InfoRemoteFilesRequest(file = fileId, external_id = externalId)
        )
        .map(_.file)
    )

  def updateRemoteFile(
    fileId: Option[String] = None,
    externalId: Option[String] = None,
    title: Option[String] = None,
    fileType: Option[String] = None,
    externalUrl: Option[String] = None,
    previewImage: Option[String] = None,
    indexableFileContents: Option[String] = None
  ): URIO[AccessToken, SlackResponse[File]] =
    apiCall(
      RemoteFiles
        .updateRemoteFiles(
          UpdateRemoteFilesRequest(
            file = fileId,
            external_id = externalId,
            title = title,
            filetype = fileType,
            external_url = externalUrl,
            preview_image = previewImage,
            indexable_file_contents = indexableFileContents
          )
        )
        .map(_.file)
    )
}

private[slack] trait RemoteFilesAccessors { self: Slack.type =>
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ): URIO[Slack with AccessToken, SlackResponse[File]] =
    ZIO.serviceWithZIO[Slack](
      _.addRemoteFile(externalId, externalUrl, title, fileType, indexableFileContents, previewImage)
    )
}

case class AddRemoteFilesRequest(
  external_id: String,
  external_url: String,
  title: String,
  file_type: Option[String] = None,
  indexable_file_contents: Option[Chunk[Byte]] = None,
  preview_image: Option[Chunk[Byte]] = None
)

case class AddRemoteFilesResponse(
  file: File
)

object AddRemoteFilesResponse {
  implicit val decoder: Decoder[AddRemoteFilesResponse] =
    Decoder.forProduct1("file")(AddRemoteFilesResponse.apply)
}

object RemoteFiles extends GeneratedFiles {
  def addRemoteFiles(req: AddRemoteFilesRequest): Request[AddRemoteFilesResponse, AccessToken] = {
    val multiPart1 = req.file_type.map(multipart("filetype", _))
    val multiPart2 = req.indexable_file_contents.map(chunk => multipart("indexable_file_contents", chunk.toArray))
    val multiPart3 = req.preview_image.map(chunk => multipart("preview_image", chunk.toArray))
    val multiPart4 = Some(multipart("external_id", req.external_id))
    val multiPart5 = Some(multipart("external_url", req.external_url))
    val multiPart6 = Some(multipart("title", req.title))

    val entities = List(multiPart1, multiPart2, multiPart3, multiPart4, multiPart5, multiPart6).flatten

    request("files.remote.add")
      .partBody(entities)
      .as[AddRemoteFilesResponse]
      .auth
      .accessToken
  }
}
