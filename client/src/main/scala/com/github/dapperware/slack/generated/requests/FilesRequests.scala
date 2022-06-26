/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param file File to delete a comment from.
 * @param id The comment to delete.
 */
case class DeleteCommentsFilesRequest(file: Option[String], id: Option[String])

object DeleteCommentsFilesRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteCommentsFilesRequest] = deriveEncoder[DeleteCommentsFilesRequest]
}

/**
 * @param file ID of file to delete.
 */
case class DeleteFilesRequest(file: Option[String])

object DeleteFilesRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[DeleteFilesRequest] = deriveEncoder[DeleteFilesRequest]
}

/**
 * @param user Filter files created by a single user.
 * @param channel Filter files appearing in a specific channel, indicated by its ID.
 * @param ts_from Filter files created after this timestamp (inclusive).
 * @param ts_to Filter files created before this timestamp (inclusive).
 * @param types Filter files by type ([see below](#file_types)). You can pass multiple values in the types argument, like `types=spaces,snippets`.The default value is `all`, which does not filter the list.
 * @param count undefined
 * @param page undefined
 * @param show_files_hidden_by_limit Show truncated file info for files hidden due to being too old, and the team who owns the file being over the file limit.
 */
case class ListFilesRequest(
  user: Option[String],
  channel: Option[String],
  ts_from: Option[Int],
  ts_to: Option[Int],
  types: Option[String],
  count: Option[String],
  page: Option[String],
  show_files_hidden_by_limit: Option[Boolean]
)

object ListFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListFilesRequest] = FormEncoder.fromParams.contramap[ListFilesRequest] { req =>
    List(
      "user"                       -> req.user,
      "channel"                    -> req.channel,
      "ts_from"                    -> req.ts_from,
      "ts_to"                      -> req.ts_to,
      "types"                      -> req.types,
      "count"                      -> req.count,
      "page"                       -> req.page,
      "show_files_hidden_by_limit" -> req.show_files_hidden_by_limit
    )
  }
}

/**
 * @param external_id Creator defined GUID for the file.
 * @param title Title of the file being shared.
 * @param filetype type of file
 * @param external_url URL of the remote file.
 * @param preview_image Preview of the document via `multipart/form-data`.
 * @param indexable_file_contents A text file (txt, pdf, doc, etc.) containing textual search terms that are used to improve discovery of the remote file.
 */
case class AddRemoteFilesRequest(
  external_id: Option[String],
  title: Option[String],
  filetype: Option[String],
  external_url: Option[String],
  preview_image: Option[String],
  indexable_file_contents: Option[String]
)

object AddRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AddRemoteFilesRequest] = FormEncoder.fromParams.contramap[AddRemoteFilesRequest] {
    req =>
      List(
        "external_id"             -> req.external_id,
        "title"                   -> req.title,
        "filetype"                -> req.filetype,
        "external_url"            -> req.external_url,
        "preview_image"           -> req.preview_image,
        "indexable_file_contents" -> req.indexable_file_contents
      )
  }
}

/**
 * @param file Specify a file by providing its ID.
 * @param external_id Creator defined GUID for the file.
 */
case class InfoRemoteFilesRequest(file: Option[String], external_id: Option[String])

object InfoRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[InfoRemoteFilesRequest] = FormEncoder.fromParams.contramap[InfoRemoteFilesRequest] {
    req =>
      List("file" -> req.file, "external_id" -> req.external_id)
  }
}

/**
 * @param channel Filter files appearing in a specific channel, indicated by its ID.
 * @param ts_from Filter files created after this timestamp (inclusive).
 * @param ts_to Filter files created before this timestamp (inclusive).
 * @param limit The maximum number of items to return.
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 */
case class ListRemoteFilesRequest(
  channel: Option[String],
  ts_from: Option[Int],
  ts_to: Option[Int],
  limit: Option[Int],
  cursor: Option[String]
)

object ListRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListRemoteFilesRequest] = FormEncoder.fromParams.contramap[ListRemoteFilesRequest] {
    req =>
      List(
        "channel" -> req.channel,
        "ts_from" -> req.ts_from,
        "ts_to"   -> req.ts_to,
        "limit"   -> req.limit,
        "cursor"  -> req.cursor
      )
  }
}

/**
 * @param file Specify a file by providing its ID.
 * @param external_id Creator defined GUID for the file.
 */
case class RemoveRemoteFilesRequest(file: Option[String], external_id: Option[String])

object RemoveRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RemoveRemoteFilesRequest] =
    FormEncoder.fromParams.contramap[RemoveRemoteFilesRequest] { req =>
      List("file" -> req.file, "external_id" -> req.external_id)
    }
}

/**
 * @param file Specify a file registered with Slack by providing its ID. Either this field or `external_id` or both are required.
 * @param external_id The globally unique identifier (GUID) for the file, as set by the app registering the file with Slack.  Either this field or `file` or both are required.
 * @param channels Comma-separated list of channel IDs where the file will be shared.
 */
case class ShareRemoteFilesRequest(file: Option[String], external_id: Option[String], channels: Option[String])

object ShareRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ShareRemoteFilesRequest] =
    FormEncoder.fromParams.contramap[ShareRemoteFilesRequest] { req =>
      List("file" -> req.file, "external_id" -> req.external_id, "channels" -> req.channels)
    }
}

/**
 * @param file Specify a file by providing its ID.
 * @param external_id Creator defined GUID for the file.
 * @param title Title of the file being shared.
 * @param filetype type of file
 * @param external_url URL of the remote file.
 * @param preview_image Preview of the document via `multipart/form-data`.
 * @param indexable_file_contents File containing contents that can be used to improve searchability for the remote file.
 */
case class UpdateRemoteFilesRequest(
  file: Option[String],
  external_id: Option[String],
  title: Option[String],
  filetype: Option[String],
  external_url: Option[String],
  preview_image: Option[String],
  indexable_file_contents: Option[String]
)

object UpdateRemoteFilesRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[UpdateRemoteFilesRequest] =
    FormEncoder.fromParams.contramap[UpdateRemoteFilesRequest] { req =>
      List(
        "file"                    -> req.file,
        "external_id"             -> req.external_id,
        "title"                   -> req.title,
        "filetype"                -> req.filetype,
        "external_url"            -> req.external_url,
        "preview_image"           -> req.preview_image,
        "indexable_file_contents" -> req.indexable_file_contents
      )
    }
}
