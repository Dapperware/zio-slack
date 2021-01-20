package com.dapperware.slack

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

  implicit val confirmFieldFmt        = deriveCodec[ConfirmField]
  implicit val actionFieldFmt         = deriveCodec[ActionField]
  implicit val attachmentFieldFmt     = deriveCodec[AttachmentField]
  implicit val attachmentFmt          = deriveCodec[Attachment]
  implicit val authIdentityFmt        = deriveCodec[AuthIdentity]
  implicit val teamFmt                = deriveCodec[Team]
  implicit val channelValueFmt        = deriveCodec[ChannelValue]
  implicit val groupValueFmt          = deriveCodec[GroupValue]
  implicit val imFmt                  = deriveCodec[Im]
  implicit val channelFmt             = deriveCodec[Channel]
  implicit val groupFmt               = deriveCodec[Group]
  implicit val userProfileFmt         = deriveCodec[UserProfile]
  implicit val userFmt                = deriveCodec[User]
  implicit val reactionFmt            = deriveCodec[Reaction]
  implicit val slackCommentFmt        = deriveCodec[SlackComment]
  implicit val slackFileFmt           = deriveCodec[SlackFile]
  implicit val slackFileIdFmt         = deriveCodec[SlackFileId]
  implicit val updateResponseFmt      = deriveCodec[UpdateResponse]
  implicit val appFmt                 = deriveCodec[App]
  implicit val reactionMsgFmt         = deriveCodec[ReactionItemMessage]
  implicit val reactionFileFmt        = deriveCodec[ReactionItemFile]
  implicit val reactionFileCommentFmt = deriveCodec[ReactionItemFileComment]
  implicit val reactionItemReads = new Decoder[ReactionItem] {

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
  implicit val reactionItemWrites = Encoder.instance[ReactionItem] {
    case i: ReactionItemMessage     => i.asJson
    case i: ReactionItemFile        => i.asJson
    case i: ReactionItemFileComment => i.asJson
  }

  implicit val reminderCodec = deriveCodec[Reminder]

  implicit val optionElementFmt = deriveCodec[OptionElement]
  implicit val selectElementFmt = deriveCodec[SelectElement]
  implicit val textElementFmt   = deriveCodec[TextElement]

  implicit val dialogElementReads = new Decoder[DialogElement] {
    def apply(json: HCursor): Decoder.Result[DialogElement] =
      for {
        rType <- json.downField("type").as[String]
        element <- rType match {
                    case "select" => json.as[SelectElement]
                    case _        => json.as[TextElement]
                  }
      } yield element
  }
  implicit val dialogElementWrites = new Encoder[DialogElement] {

    override def apply(element: DialogElement): Json = element match {
      case e: TextElement   => e.asJson
      case e: SelectElement => e.asJson
    }
  }
  implicit val dialogFmt = deriveCodec[Dialog]

}
