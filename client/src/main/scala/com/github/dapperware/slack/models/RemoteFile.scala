package com.github.dapperware.slack.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class RemoteFile(
  id: String,
  created: Long,
  timestamp: Long,
  name: String,
  title: String,
  mimetype: String,
  filetype: String,
  pretty_type: String,
  user: String,
  editable: Boolean,
  size: Long,
  mode: String,
  is_external: Boolean,
  external_type: String,
  is_public: Boolean,
  public_url_shared: Boolean,
  display_as_bot: Boolean,
  username: String,
  url_private: String,
  permalink: String,
  comments_count: Int,
  is_starred: Boolean,
  shares: Map[String, String],
  channels: List[String],
  groups: List[String],
  ims: List[String],
  external_id: String,
  external_url: String,
  has_rich_preview: Boolean
)

object RemoteFile {
  implicit val decoder: Decoder[RemoteFile] = deriveDecoder
}
