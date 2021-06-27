package com.github.dapperware.slack.api

import com.github.dapperware.slack.{ SlackEnv, SlackError }
import com.github.dapperware.slack.models.RemoteFile
import zio.ZIO
import sttp.client3._

trait SlackRemoteFiles {
  def addRemoteFile(
    externalId: String,
    externalUrl: String,
    title: String,
    fileType: Option[String] = None,
    indexableFileContents: Option[Array[Byte]] = None,
    previewImage: Option[Array[Byte]] = None
  ): ZIO[SlackEnv, SlackError, RemoteFile] = {
    val formPart   = Map("external_id" -> externalId, "external_url" -> externalUrl, "title" -> title) ++ fileType.fold(
      Map.empty[String, String]
    )(ft => Map("file_type" -> ft))
    
    val multiPart1 = Some(multipart("form_part", formPart))
    val multiPart2 = indexableFileContents.map(multipart("indexable_file_contents", _))
    val multiPart3 = previewImage.map(multipart("preview_image", _))

    val entities = List(multiPart1, multiPart2, multiPart3).flatten

    val request = basicRequest.multipartBody(entities)

    sendM(
      requestEntity("files.remote.add", request)
    ) >>= as[RemoteFile]("file")
  }
}
