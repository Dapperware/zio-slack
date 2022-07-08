package com.github.dapperware.slack.models

import com.github.dapperware.slack.models.File.{ FileMeta, Preview, Sharing, Thumbs, Video }
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class File(
  id: String,
  meta: FileMeta,
  preview: Preview = Preview(),
  video: Video = Video(),
  sharing: Sharing = Sharing(),
  thumbs: Thumbs = Thumbs()
)

object File {

  final case class FileMeta(
    created: Int,
    timestamp: Int,
    name: Option[String] = None,
    title: Option[String] = None,
    subject: Option[String] = None,
    mimetype: Option[String] = None,
    filetype: Option[String] = None,
    pretty_type: Option[String] = None,
    user: Option[String] = None,
    username: Option[String] = None,
    mode: Option[String] = None,
    editable: Option[Boolean] = None,
    non_owner_editable: Option[Boolean] = None, // application/vnd.slack-docs
    editor: Option[String] = None,
    last_editor: Option[String] = None,
    updated: Option[Int] = None,
    size: Option[Long] = None,
    lines: Option[Int] = None,
    lines_more: Option[Int] = None
  )

  object FileMeta {
    implicit val decoder: Decoder[FileMeta] = deriveDecoder[FileMeta]
  }

  final case class Transcription(
    status: Option[String],
    locale: Option[String]
  )

  object Transcription {
    implicit val decoder: Decoder[Transcription] = deriveDecoder[Transcription]
  }

  final case class Video(
    transcription: Option[Transcription] = None,
    mp4: Option[String] = None,
    vtt: Option[String] = None,
    hls: Option[String] = None,
    hls_embed: Option[String] = None,
    duration_ms: Option[Int] = None,
    thumb_video_w: Option[Int] = None,
    thumb_video_h: Option[Int] = None
  )

  object Video {
    implicit val decoder: Decoder[Video] = deriveDecoder[Video]
  }

  final case class ThumbPdf(
    thumb_gif: Option[String] = None,
    thumb_pdf: Option[String] = None,
    thumb_pdf_w: Option[String] = None,
    thumb_pdf_h: Option[String] = None
  )

  object ThumbPdf {
    val decoder: Decoder[Option[ThumbPdf]] = deriveDecoder[ThumbPdf].map(Some(_).filter(_.thumb_pdf.isDefined))
  }

  final case class Thumbs(
    _64: Option[Thumb] = None,
    _80: Option[Thumb] = None,
    _160: Option[Thumb] = None,
    _360: Option[Thumb] = None,
    _480: Option[Thumb] = None,
    _720: Option[Thumb] = None,
    _800: Option[Thumb] = None,
    _960: Option[Thumb] = None,
    _1024: Option[Thumb] = None,
    pdf: Option[ThumbPdf] = None
  )

  final case class Thumb(
    image: Option[String],
    gif: Option[String],
    width: Option[String],
    height: Option[String]
  )

  object Thumb {
    def decoder(size: Int): Decoder[Option[Thumb]] = Decoder.instance { c =>
      for {
        image  <- c.downField("thumb_" + size).as[Option[String]]
        gif    <- c.downField(s"thumb_${size}_gif").as[Option[String]]
        width  <- c.downField(s"thumb_${size}_w").as[Option[String]]
        height <- c.downField(s"thumb_${size}_h").as[Option[String]]
      } yield Some(Thumb(image, gif, width, height)).filter(_.image.isDefined)
    }
  }

  final case class Preview(
    has_rich_preview: Option[Boolean] = None,
    media_display_type: Option[String] = None,
    preview_is_truncated: Option[Boolean] = None,
    preview: Option[String] = None,
    preview_highlight: Option[String] = None
  )

  object Preview {
    val decoder: Decoder[Preview] = deriveDecoder[Preview]
  }

  final case class Sharing(
    is_external: Boolean = false,
    external_type: Option[String] = None,
    external_id: Option[String] = None,
    external_url: Option[String] = None,
    permalink: Option[String] = None,
    permalink_public: Option[String] = None,
    url_download: Option[String] = None,
    url_private: Option[String] = None,
    url_private_download: Option[String] = None,
    edit_link: Option[String] = None,
    public_url_shared: Option[Boolean] = None,
    display_as_bot: Option[Boolean] = None,
    is_public: Option[Boolean] = None,
    channels: Option[List[String]] = None,
    groups: Option[List[String]] = None,
    ims: Option[List[String]] = None,
    shares: Option[Shares] = None
  )

  object Sharing {
    implicit val decoder: Decoder[Sharing] = deriveDecoder[Sharing]
  }

  implicit val decoder: Decoder[File] = Decoder.instance { c =>
    for {
      id      <- c.downField("id").as[String]
      meta    <- FileMeta.decoder(c)
      _64     <- Thumb.decoder(64)(c)
      _80     <- Thumb.decoder(80)(c)
      _160    <- Thumb.decoder(160)(c)
      _360    <- Thumb.decoder(360)(c)
      _480    <- Thumb.decoder(480)(c)
      _720    <- Thumb.decoder(720)(c)
      _800    <- Thumb.decoder(800)(c)
      _960    <- Thumb.decoder(960)(c)
      _1024   <- Thumb.decoder(1024)(c)
      pdf     <- ThumbPdf.decoder(c)
      sharing <- Sharing.decoder(c)
      preview <- Preview.decoder(c)
      video   <- Video.decoder(c)
    } yield File(
      id = id,
      meta = meta,
      sharing = sharing,
      video = video,
      preview = preview,
      thumbs = Thumbs(
        _64 = _64,
        _80 = _80,
        _160 = _160,
        _360 = _360,
        _480 = _480,
        _720 = _720,
        _800 = _800,
        _960 = _960,
        _1024 = _1024,
        pdf = pdf
      )
    )
  }

}
