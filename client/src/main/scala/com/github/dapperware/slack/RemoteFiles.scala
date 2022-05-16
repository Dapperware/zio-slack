package com.github.dapperware.slack

import com.github.dapperware.slack.Slack.request
import com.github.dapperware.slack.models.RemoteFile
import sttp.client3.multipart
import zio.Chunk

trait RemoteFiles {
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Chunk[Byte]] = None,
    previewImage: Option[Chunk[Byte]] = None
  ) = {
    val multiPart1 = fileType.map(multipart("filetype", _))
    val multiPart2 = indexableFileContents.map(chunk => multipart("indexable_file_contents", chunk.toArray))
    val multiPart3 = previewImage.map(chunk => multipart("preview_image", chunk.toArray))
    val multiPart4 = Some(multipart("external_id", externalId))
    val multiPart5 = Some(multipart("external_url", externalUrl))
    val multiPart6 = Some(multipart("title", title))

    val entities = List(multiPart1, multiPart2, multiPart3, multiPart4, multiPart5, multiPart6).flatten

    request("files.remote.add")
      .partBody(entities)
      .at[RemoteFile]("file")
  }
}
