package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.RemoteFile
import io.circe.Decoder
import sttp.client3.multipart
import zio.{ Chunk, Has, URIO }

trait RemoteFiles { self: Slack =>
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ): URIO[Has[AccessToken], SlackResponse[RemoteFile]] =
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
}

private[slack] trait RemoteFilesAccessors {
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ): URIO[Has[Slack] with Has[AccessToken], SlackResponse[RemoteFile]] =
    URIO.accessM(_.get.addRemoteFile(externalId, externalUrl, title, fileType, indexableFileContents, previewImage))
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
  file: RemoteFile
)

object AddRemoteFilesResponse {
  implicit val decoder: Decoder[AddRemoteFilesResponse] =
    Decoder.forProduct1("file")(AddRemoteFilesResponse.apply)
}

object RemoteFiles {
  def addRemoteFiles(req: AddRemoteFilesRequest) = {
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
  }
}
