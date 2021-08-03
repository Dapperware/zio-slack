package com.github.dapperware.slack

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

package object models {

  def eitherObjectFormat[A, B](leftKey: String, rightKey: String)(implicit aFormat: Codec[A],
                                                                  bFormat: Codec[B]): Codec[Either[A, B]] =
    Codec.from(
      new Decoder[Either[A, B]] {
        override def apply(c: HCursor): Decoder.Result[Either[A, B]] =
          c.downField(leftKey).as[String].toOption match {
            case Some(_) => c.as[A].map(Left(_))
            case None    => c.as[B].map(Right(_))
          }
      },
      new Encoder[Either[A, B]] {
        override def apply(o: Either[A, B]): Json =
          o match {
            case Left(a)  => a.asJson
            case Right(b) => b.asJson
          }
      }
    )

  implicit val confirmFieldFmt: Codec.AsObject[ConfirmField]        = deriveCodec[ConfirmField]
  implicit val actionFieldFmt: Codec.AsObject[ActionField]         = deriveCodec[ActionField]
  implicit val attachmentFieldFmt: Codec.AsObject[AttachmentField]     = deriveCodec[AttachmentField]
  implicit val attachmentFmt: Codec.AsObject[Attachment] = deriveCodec[Attachment]
  implicit val authIdentityFmt: Codec.AsObject[AuthIdentity] = deriveCodec[AuthIdentity]
  implicit val teamFmt: Codec.AsObject[Team] = deriveCodec[Team]
  implicit val channelValueFmt: Codec.AsObject[ChannelValue] = deriveCodec[ChannelValue]
  implicit val groupValueFmt: Codec.AsObject[GroupValue]          = deriveCodec[GroupValue]
  implicit val imFmt: Codec.AsObject[Im]                  = deriveCodec[Im]
  implicit val channelFmt: Codec.AsObject[Channel]             = deriveCodec[Channel]
  implicit val groupFmt: Codec.AsObject[Group]               = deriveCodec[Group]
  implicit val userProfileFmt: Codec.AsObject[UserProfile]         = deriveCodec[UserProfile]
  implicit val userFmt: Codec.AsObject[User]                = deriveCodec[User]
  implicit val reactionFmt: Codec.AsObject[Reaction]            = deriveCodec[Reaction]
  implicit val slackCommentFmt: Codec.AsObject[SlackComment]        = deriveCodec[SlackComment]
  implicit val slackFileFmt: Codec.AsObject[SlackFile]           = deriveCodec[SlackFile]
  implicit val slackFileIdFmt: Codec.AsObject[SlackFileId]         = deriveCodec[SlackFileId]
  implicit val updateResponseFmt: Codec.AsObject[UpdateResponse]      = deriveCodec[UpdateResponse]
  implicit val appFmt: Codec.AsObject[App]                 = deriveCodec[App]
  implicit val reactionMsgFmt: Codec.AsObject[ReactionItemMessage]         = deriveCodec[ReactionItemMessage]
  implicit val reactionFileFmt: Codec.AsObject[ReactionItemFile]        = deriveCodec[ReactionItemFile]
  implicit val reactionFileCommentFmt: Codec.AsObject[ReactionItemFileComment] = deriveCodec[ReactionItemFileComment]
  implicit val reactionItemReads: Decoder[ReactionItem] = new Decoder[ReactionItem] {

    override def apply(c: HCursor): Result[ReactionItem] =
      for {
        typ <- c.downField("type").as[String]
        result <- typ match {
                   case "message"      => c.as[ReactionItemMessage]
                   case "file"         => c.as[ReactionItemFile]
                   case "file_comment" => c.as[ReactionItemFileComment]
                   case t: String      => Left(DecodingFailure(s"Invalid type property, $t", List.empty))
                 }
      } yield result
  }
  implicit val reactionItemWrites: Encoder[ReactionItem] = Encoder.instance[ReactionItem] {
    case i: ReactionItemMessage     => i.asJson
    case i: ReactionItemFile        => i.asJson
    case i: ReactionItemFileComment => i.asJson
  }

  implicit val reminderCodec: Codec.AsObject[Reminder] = deriveCodec[Reminder]

  implicit val optionElementFmt: Codec.AsObject[OptionElement] = deriveCodec[OptionElement]
  implicit val selectElementFmt: Codec.AsObject[SelectElement] = deriveCodec[SelectElement]
  implicit val textElementFmt: Codec.AsObject[TextElement]   = deriveCodec[TextElement]

  implicit val dialogElementReads: Decoder[DialogElement] = new Decoder[DialogElement] {
    def apply(json: HCursor): Decoder.Result[DialogElement] =
      for {
        rType <- json.downField("type").as[String]
        element <- rType match {
                    case "select" => json.as[SelectElement]
                    case _        => json.as[TextElement]
                  }
      } yield element
  }
  implicit val dialogElementWrites: Encoder[DialogElement] = new Encoder[DialogElement] {

    override def apply(element: DialogElement): Json = element match {
      case e: TextElement   => e.asJson
      case e: SelectElement => e.asJson
    }
  }
  implicit val dialogFmt: Codec.AsObject[Dialog] = deriveCodec[Dialog]

}
