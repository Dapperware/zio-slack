package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import com.github.dapperware.slack.models.RemoteFile
import zio.{ZIO, Chunk}
import sttp.client3._

trait SlackRemoteFiles {
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ): ZIO[SlackEnv, SlackError, RemoteFile] = {
    val multiPart1 = fileType.map(multipart("filetype", _))
    val multiPart2 = indexableFileContents.map(chunk => multipart("indexable_file_contents", chunk.toArray))
    val multiPart3 = previewImage.map(chunk => multipart("preview_image", chunk.toArray))
    val multiPart4 = Some(multipart("external_id", externalId))
    val multiPart5 = Some(multipart("external_url", externalUrl))
    val multiPart6 = Some(multipart("title", title))

    val entities = List(multiPart1, multiPart2, multiPart3, multiPart4, multiPart5, multiPart6).flatten

    val request = basicRequest.multipartBody(entities)

    sendM(
      requestEntity("files.remote.add", request)
    ) >>= as[RemoteFile]("file")
  }
}
