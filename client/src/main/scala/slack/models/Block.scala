package slack.models

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

sealed trait Block {
  val `type`: String
  val block_id: Option[String]
}

case class Divider(block_id: Option[String] = None) extends Block {
  override val `type`: String = "divider"
}

case class Section(text: TextObject,
                   fields: Option[Seq[TextObject]],
                   accessory: Option[BlockElement],
                   block_id: Option[String] = None)
    extends Block {
  override val `type`: String = "section"
}

case class ImageBlock(image_url: String,
                      alt_text: String,
                      title: Option[PlainTextObject],
                      block_id: Option[String] = None)
    extends Block {
  override val `type`: String = "image"
  require(title.forall(_.`type` == "plain_text"))
}

case class ActionsBlock(elements: Seq[BlockElement], block_id: Option[String] = None) extends Block {
  override val `type`: String = "actions"
  require(elements.size <= 5, "Maximum of 5 elements in each action block")
}

case class ContextBlock(elements: Seq[Either[ImageElement, TextObject]], block_id: Option[String] = None)
    extends Block {
  override val `type`: String = "context"
}

trait TextObject {
  val `type`: String
  val text: String
}

case class PlainTextObject(text: String, emoji: Option[Boolean] = None, `type`: String = "plain_text")
    extends TextObject

case class MarkdownTextObject(text: String, verbatim: Option[Boolean] = None, `type`: String = "mrkdwn")
    extends TextObject

object TextObject {
  implicit private[slack] val plainTextFmt: Codec[PlainTextObject] = deriveCodec[PlainTextObject]
  implicit private[slack] val mrkdwnTextFmt: Codec[MarkdownTextObject] = deriveCodec[MarkdownTextObject]

  private val textWrites = new Encoder[TextObject] {
    def apply(text: TextObject): Json = {
      val json = text match {
        case t: PlainTextObject    => t.asJson
        case t: MarkdownTextObject => t.asJson
      }
      Json.obj("type" -> text.`type`.asJson).deepMerge(json)
    }
  }
  private val textReads = new Decoder[TextObject] {
    def apply(c: HCursor): Decoder.Result[TextObject] =
      for {
        value <- c.downField("type").as[String]
        result <- value match {
          case "plain_text" => c.as[PlainTextObject]
          case "mrkdwn"     => c.as[MarkdownTextObject]
          case other        => Left(DecodingFailure(s"Invalid text object type: $other", List.empty))
        }
      } yield result
  }

  implicit val format = Codec.from(textReads, textWrites)
}

case class OptionObject(text: PlainTextObject, value: String)

case class OptionGroupObject(label: PlainTextObject, options: Seq[OptionObject])

case class ConfirmationObject(title: PlainTextObject, text: TextObject, confirm: PlainTextObject, deny: PlainTextObject)

trait BlockElement {
  val `type`: String
}

case class ImageElement(image_url: String, alt_text: String, `type`: String = "image") extends BlockElement {}

case class ButtonElement(text: PlainTextObject,
                         action_id: String,
                         url: Option[String],
                         value: Option[String],
                         confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "button"
}

case class StaticSelectElement(placeholder: PlainTextObject,
                               action_id: String,
                               options: Seq[OptionObject],
                               option_groups: Seq[OptionGroupObject],
                               initial_option: Option[Either[OptionObject, OptionGroupObject]],
                               confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "static_select"
}

case class ExternalSelectElement(placeholder: PlainTextObject,
                                 action_id: String,
                                 min_query_length: Option[Int],
                                 initial_option: Option[Either[OptionObject, OptionGroupObject]],
                                 confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "external_select"
}

case class UserSelectElement(placeholder: PlainTextObject,
                             action_id: String,
                             initial_user: Option[String],
                             confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "users_select"
}

case class ChannelSelectElement(placeholder: PlainTextObject,
                                action_id: String,
                                initial_channel: Option[String],
                                confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "channels_select"
}

case class ConversationSelectElement(placeholder: PlainTextObject,
                                     action_id: String,
                                     initial_conversation: Option[String],
                                     confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "conversations_select"
}

case class OverflowElement(action_id: String, options: Seq[OptionObject], confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "overflow"
}

case class DatePickerElement(action_id: String,
                             placeholder: PlainTextObject,
                             initial_date: Option[String],
                             confirm: Option[ConfirmationObject])
    extends BlockElement {
  override val `type`: String = "datepicker"
}

object BlockElement {
  implicit val plainTextFmt = deriveCodec[PlainTextObject]

  implicit val optionObjFmt = deriveCodec[OptionObject]
  implicit val optionGrpObjFmt = deriveCodec[OptionGroupObject]
  implicit val confirmObjFmt = deriveCodec[ConfirmationObject]

  implicit val eitherOptFmt = eitherObjectFormat[OptionObject, OptionGroupObject]("text", "label")
  implicit val buttonElementFmt = deriveCodec[ButtonElement]
  implicit val imageElementFmt = deriveCodec[ImageElement]
  implicit val staticMenuElementFmt = deriveCodec[StaticSelectElement]
  implicit val extMenuElementFmt = deriveCodec[ExternalSelectElement]
  implicit val userMenuElementFmt = deriveCodec[UserSelectElement]
  implicit val channelMenuElementFmt = deriveCodec[ChannelSelectElement]
  implicit val conversationMenuElementFmt = deriveCodec[ConversationSelectElement]
  implicit val overflowElementFmt = deriveCodec[OverflowElement]
  implicit val datePickerElementFmt = deriveCodec[DatePickerElement]

  private val elemWrites = new Encoder[BlockElement] {
    def apply(element: BlockElement): Json = {
      val json = element match {
        case elem: ButtonElement             => elem.asJson
        case elem: ImageElement              => elem.asJson
        case elem: StaticSelectElement       => elem.asJson
        case elem: ExternalSelectElement     => elem.asJson
        case elem: UserSelectElement         => elem.asJson
        case elem: ChannelSelectElement      => elem.asJson
        case elem: ConversationSelectElement => elem.asJson
        case elem: OverflowElement           => elem.asJson
        case elem: DatePickerElement         => elem.asJson
      }
      Json.obj("type" -> element.`type`.asJson).deepMerge(json)
    }
  }
  private val elemReads = new Decoder[BlockElement] {

    override def apply(c: HCursor): Result[BlockElement] =
      for {
        value <- c.downField("type").as[String]
        result <- value match {
          case "button"               => c.as[ButtonElement]
          case "image"                => c.as[ImageElement]
          case "static_select"        => c.as[StaticSelectElement]
          case "external_select"      => c.as[ExternalSelectElement]
          case "users_select"         => c.as[UserSelectElement]
          case "conversations_select" => c.as[ConversationSelectElement]
          case "channels_select"      => c.as[ChannelSelectElement]
          case "overflow"             => c.as[OverflowElement]
          case "datepicker"           => c.as[DatePickerElement]
          case other                  => Left(DecodingFailure(s"Invalid element type: $other", List.empty))
        }
      } yield result
  }

  implicit val format = Codec.from(elemReads, elemWrites)
}

object Block {
  implicit val plainTextFmt = deriveCodec[PlainTextObject]
  implicit val imageElementFmt = deriveCodec[ImageElement]

  implicit val eitherContextFmt = eitherObjectFormat[ImageElement, TextObject]("image_url", "text")
  implicit val dividerFmt = deriveCodec[Divider]
  implicit val imageBlockFmt = deriveCodec[ImageBlock]
  implicit val actionBlockFmt = deriveCodec[ActionsBlock]
  implicit val contextBlockFmt = deriveCodec[ContextBlock]
  implicit val sectionFmt = deriveCodec[Section]

  private val blockWrites = new Encoder[Block] {

    override def apply(block: Block): Json = {
      val json = block match {
        case b: Divider      => b.asJson
        case b: Section      => b.asJson
        case b: ImageBlock   => b.asJson
        case b: ActionsBlock => b.asJson
        case b: ContextBlock => b.asJson
      }
      Json.obj("type" -> block.`type`.asJson).deepMerge(json)
    }
  }

  private val blockReads = new Decoder[Block] {

    override def apply(c: HCursor): Result[Block] =
      for {
        value <- c.downField("type").as[String]
        result <- value match {
          case "divider" => c.as[Divider]
          case "image"   => c.as[ImageBlock]
          case "actions" => c.as[ActionsBlock]
          case "context" => c.as[ContextBlock]
          case "section" => c.as[Section]
          case other     => Left(DecodingFailure(s"Invalid block type: $other", List.empty))
        }
      } yield result
  }

  implicit val format: Codec[Block] = Codec.from(blockReads, blockWrites)
}
