package com.github.dapperware.slack.models

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import java.time.LocalDate

sealed trait Block {
  val `type`: String
  val block_id: Option[String]
}

case class Divider(block_id: Option[String] = None) extends Block {
  override val `type`: String = "divider"
}

object Divider {
  implicit val decoder: Decoder[Divider]          = deriveDecoder[Divider]
  implicit val encoder: Encoder.AsObject[Divider] = deriveEncoder[Divider]
}

case class Section(
  text: TextObject,
  fields: Option[Seq[TextObject]] = None,
  accessory: Option[BlockElement] = None,
  block_id: Option[String] = None
) extends Block {
  override val `type`: String = "section"
}

case class ImageBlock(
  image_url: String,
  alt_text: String,
  title: Option[PlainTextObject],
  block_id: Option[String] = None
) extends Block {
  override val `type`: String = "image"
  require(title.forall(_.`type` == "plain_text"))
}

object ImageBlock {
  implicit val decoder: Decoder[ImageBlock]          = deriveDecoder[ImageBlock]
  implicit val encoder: Encoder.AsObject[ImageBlock] = deriveEncoder[ImageBlock]
}

case class ActionsBlock(elements: Seq[BlockElement], block_id: Option[String] = None) extends Block {
  override val `type`: String = "actions"
  require(elements.size <= 5, "Maximum of 5 elements in each action block")
}

case class HeaderBlock(text: PlainTextObject, block_id: Option[String] = None) extends Block {
  override val `type`: String = "header"
}

case class ContextBlock(elements: Seq[Either[ImageElement, TextObject]], block_id: Option[String] = None)
    extends Block {
  override val `type`: String = "context"
}

case class RichTextBlock(elements: Seq[BlockElement], block_id: Option[String] = None) extends Block {
  override val `type`: String = "rich_text"
}

case class InputBlock(
  label: PlainTextObject,
  element: InputBlockElement,
  dispatch_action: Option[Boolean] = None,
  block_id: Option[String] = None,
  hint: Option[PlainTextObject] = None,
  optional: Option[Boolean] = None
) extends Block {
  override val `type` = "input"
}

sealed trait InputBlockElement {
  self: BlockElement =>

  val `type`: String
}

case class PlainTextInput(
  action_id: String,
  placeholder: Option[PlainTextObject] = None,
  initial_value: Option[String] = None,
  multiline: Option[Boolean] = None,
  min_length: Option[Int] = None,
  max_length: Option[Int] = None,
  dispatch_action_config: Option[DispatchActionConfig] = None
) extends BlockElement
    with InputBlockElement {
  override val `type` = "plain_text_input"
}

object PlainTextInput {
  implicit val decoder: Decoder[PlainTextInput]          = deriveDecoder[PlainTextInput]
  implicit val encoder: Encoder.AsObject[PlainTextInput] = deriveEncoder[PlainTextInput]
}

case class DispatchActionConfig(trigger_actions_on: List[TriggerAction])

object DispatchActionConfig {
  implicit val codec: Codec[DispatchActionConfig] = deriveCodec[DispatchActionConfig]
}

sealed trait TriggerAction

object TriggerAction {
  case object OnEnterPressed     extends TriggerAction
  case object OnCharacterEntered extends TriggerAction

  implicit val encoder: Encoder[TriggerAction] = Encoder.instance {
    case OnEnterPressed     => "on_enter_pressed".asJson
    case OnCharacterEntered => "on_character_entered".asJson
  }

  implicit val decoder: Decoder[TriggerAction] = Decoder.decodeString.emap[TriggerAction] {
    case "on_enter_pressed"     => Right(OnEnterPressed)
    case "on_character_entered" => Right(OnCharacterEntered)
    case s                      => Left(s"Could not decode TriggerAction from $s")
  }
}

trait TextObject {
  val `type`: String
  val text: String
}

case class PlainTextObject(text: String, emoji: Option[Boolean] = None, `type`: String = "plain_text")
    extends TextObject

object PlainTextObject {
  implicit val codec: Codec.AsObject[PlainTextObject] = deriveCodec[PlainTextObject]
}

case class MarkdownTextObject(text: String, verbatim: Option[Boolean] = None, `type`: String = "mrkdwn")
    extends TextObject

object MarkdownTextObject {
  implicit val codec: Codec.AsObject[MarkdownTextObject] = deriveCodec[MarkdownTextObject]
}

object TextObject {

  private val textEncoder = Encoder.AsObject.instance[TextObject] { text =>
    val json = text match {
      case t: PlainTextObject    => t.asJsonObject
      case t: MarkdownTextObject => t.asJsonObject
    }
    json.add("type", text.`type`.asJson)
  }

  private val textDecoder: Decoder[TextObject] = Decoder.instance[TextObject] { c =>
    for {
      value  <- c.downField("type").as[String]
      result <- value match {
                  case "plain_text" => c.as[PlainTextObject]
                  case "mrkdwn"     => c.as[MarkdownTextObject]
                  case other        => Left(DecodingFailure(s"Invalid text object type: $other", List.empty))
                }
    } yield result
  }

  implicit val format: Codec[TextObject] = Codec.from(textDecoder, textEncoder)
}

case class OptionObject(text: PlainTextObject, value: String)

object OptionObject {
  implicit val codec: Codec[OptionObject] = deriveCodec[OptionObject]
}

case class OptionGroupObject(label: PlainTextObject, options: Seq[OptionObject])

object OptionGroupObject {
  implicit val codec: Codec[OptionGroupObject] = deriveCodec[OptionGroupObject]
}

case class ConfirmationObject(title: PlainTextObject, text: TextObject, confirm: PlainTextObject, deny: PlainTextObject)

object ConfirmationObject {
  implicit val codec: Codec[ConfirmationObject] = deriveCodec[ConfirmationObject]
}

trait BlockElement {
  val `type`: String
}

case class ImageElement(image_url: String, alt_text: String, `type`: String = "image") extends BlockElement {}

object ImageElement {
  implicit val codec: Codec[ImageElement] = deriveCodec[ImageElement]
}

case class ButtonElement(
  text: PlainTextObject,
  action_id: String,
  url: Option[String] = None,
  value: Option[String] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement {
  override val `type`: String = "button"
}

object ButtonElement {
  implicit val codec: Codec[ButtonElement] = deriveCodec[ButtonElement]
}

case class StaticSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  options: Seq[OptionObject],
  option_groups: Option[Seq[OptionGroupObject]] = None,
  initial_option: Option[Either[OptionObject, OptionGroupObject]] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "static_select"
}

case class ExternalSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  min_query_length: Option[Int] = None,
  initial_option: Option[Either[OptionObject, OptionGroupObject]] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement {
  override val `type`: String = "external_select"
}

case class UserSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  initial_user: Option[String] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "users_select"
}

object UserSelectElement {
  implicit val codec: Codec.AsObject[UserSelectElement] = deriveCodec[UserSelectElement]
}

case class MultiUsersSelectElement(
  placeholder: PlainTextObject,
  action_id: String
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "multi_users_select"
}

object MultiUsersSelectElement {
  implicit val codec: Codec.AsObject[MultiUsersSelectElement] = deriveCodec[MultiUsersSelectElement]
}

case class ChannelSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  initial_channel: Option[String] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement {
  override val `type`: String = "channels_select"
}

object ChannelSelectElement {
  implicit val codec: Codec[ChannelSelectElement] = deriveCodec[ChannelSelectElement]
}

case class ConversationSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  initial_conversation: Option[String] = None,
  confirm: Option[ConfirmationObject] = None,
  response_url_enabled: Option[Boolean] = None
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "conversations_select"
}

object ConversationSelectElement {
  implicit val codec: Codec.AsObject[ConversationSelectElement] = deriveCodec[ConversationSelectElement]
}

case class MultiConversationsSelectElement(
  placeholder: PlainTextObject,
  action_id: String,
  initial_conversations: Option[List[String]] = None,
  default_to_current_conversation: Option[Boolean] = None,
  confirm: Option[ConfirmationObject] = None,
  max_selected_items: Option[Int] = None,
  response_url_enabled: Option[Boolean] = None
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "multi_conversations_select"
}

object MultiConversationsSelectElement {
  implicit val codec: Codec.AsObject[MultiConversationsSelectElement] = deriveCodec[MultiConversationsSelectElement]
}

case class OverflowElement(action_id: String, options: Seq[OptionObject], confirm: Option[ConfirmationObject] = None)
    extends BlockElement {
  override val `type`: String = "overflow"
}

object OverflowElement {
  implicit val codec: Codec.AsObject[OverflowElement] = deriveCodec[OverflowElement]
}

case class DatePickerElement(
  action_id: String,
  placeholder: PlainTextObject,
  initial_date: Option[LocalDate] = None,
  confirm: Option[ConfirmationObject] = None
) extends BlockElement
    with InputBlockElement {
  override val `type`: String = "datepicker"
}

object DatePickerElement {
  implicit val codec: Codec.AsObject[DatePickerElement] = deriveCodec[DatePickerElement]
}

sealed trait RichTextElement

object RichTextElement {
  case class TextStyle(
    bold: Option[Boolean] = None,
    italic: Option[Boolean] = None,
    strike: Option[Boolean] = None,
    code: Option[Boolean] = None
  )

  object TextStyle {
    implicit val textStyleCodec: Codec.AsObject[TextStyle] = deriveCodec[TextStyle]
  }

  sealed trait Element extends RichTextElement

  case class Text(text: String, style: Option[TextStyle]) extends Element

  object Text {
    implicit val codec: Codec.AsObject[Text] = deriveCodec[Text]
  }

  case class Emoji(text: String) extends Element

  object Emoji {
    implicit val codec: Codec.AsObject[Emoji] = deriveCodec[Emoji]
  }

  case class Channel(channel_id: String, style: Option[TextStyle]) extends Element

  object Channel {
    implicit val codec: Codec.AsObject[Channel] = deriveCodec[Channel]
  }

  case class User(user_id: String, style: Option[TextStyle]) extends Element

  object User {
    implicit val codec: Codec.AsObject[User] = deriveCodec[User]
  }

  case class Link(url: String, text: Option[String], style: Option[TextStyle]) extends Element

  object Link {
    implicit val codec: Codec.AsObject[Link] = deriveCodec[Link]
  }

  case class Team(team_id: String, style: Option[TextStyle]) extends Element

  object Team {
    implicit val codec: Codec.AsObject[Team] = deriveCodec[Team]
  }

  case class UserGroup(user_group_id: String, style: Option[TextStyle]) extends Element

  object UserGroup {
    implicit val codec: Codec.AsObject[UserGroup] = deriveCodec[UserGroup]
  }

  case class Date(ts: String) extends Element

  object Date {
    implicit val codec: Codec.AsObject[Date] = deriveCodec[Date]
  }

  case class Broadcast(range: String, style: Option[TextStyle]) extends Element

  object Broadcast {
    implicit val codec: Codec.AsObject[Broadcast] = deriveCodec[Broadcast]
  }

  case class Color(value: String) extends Element

  object Color {
    implicit val codec: Codec.AsObject[Color] = deriveCodec[Color]
  }

  case class UnknownElement(`type`: String) extends Element with BlockElement

  object Element {
    implicit val elementDecoder: Decoder[Element] = Decoder.instance[Element] { c =>
      for {
        value  <- c.downField("type").as[String]
        result <- value match {
                    case "text"       => c.as[Text]
                    case "emoji"      => c.as[Emoji]
                    case "channel"    => c.as[Channel]
                    case "user"       => c.as[User]
                    case "link"       => c.as[Link]
                    case "team"       => c.as[Team]
                    case "user_group" => c.as[UserGroup]
                    case "date"       => c.as[Date]
                    case "broadcast"  => c.as[Broadcast]
                    case "color"      => c.as[Color]
                    case _            => Right(UnknownElement(value))
                  }
      } yield result
    }

    implicit val elementEncoder: Encoder.AsObject[Element] = Encoder.AsObject.instance[Element] {
      case t: Text           => t.asJsonObject
      case e: Emoji          => e.asJsonObject
      case c: Channel        => c.asJsonObject
      case u: User           => u.asJsonObject
      case l: Link           => l.asJsonObject
      case t: Team           => t.asJsonObject
      case ug: UserGroup     => ug.asJsonObject
      case d: Date           => d.asJsonObject
      case b: Broadcast      => b.asJsonObject
      case c: Color          => c.asJsonObject
      case u: UnknownElement => JsonObject("type" -> Json.fromString(u.`type`))
    }
  }

  implicit val decoder: Decoder[RichTextElement] = Decoder.instance[RichTextElement] { c =>
    for {
      value  <- c.downField("type").as[String]
      result <- value match {
                  case "rich_text_section"      => c.as[RichTextSectionElement]
                  case "rich_text_quote"        => c.as[RichTextQuoteElement]
                  case "rich_text_list"         => c.as[RichTextListElement]
                  case "rich_text_preformatted" => c.as[RichTextPreformattedElement]
                  case _                        => c.as[Element]
                }
    } yield result
  }

}

case class RichTextSectionElement(elements: Seq[RichTextElement]) extends BlockElement with RichTextElement {
  override val `type`: String = "rich_text_section"
}

object RichTextSectionElement {

  implicit val decoder: Decoder[RichTextSectionElement] = deriveDecoder[RichTextSectionElement]

}

case class RichTextQuoteElement(elements: Seq[RichTextElement]) extends BlockElement with RichTextElement {
  override val `type`: String = "rich_text_quote"
}

object RichTextQuoteElement {

  implicit val decoder: Decoder[RichTextQuoteElement] = deriveDecoder[RichTextQuoteElement]

}

case class RichTextListElement(elements: Seq[RichTextElement], style: RichTextListElement.Style, indent: Option[Int])
    extends BlockElement
    with RichTextElement {
  override val `type`: String = "rich_text_list"
}

object RichTextListElement {

  sealed trait Style
  object Style {
    case object Bulleted extends Style
    case object Numbered extends Style
  }
  implicit val styleDecoder: Decoder[Style] = deriveDecoder[Style]

  implicit val decoder: Decoder[RichTextListElement] = deriveDecoder[RichTextListElement]
}

case class RichTextPreformattedElement(elements: Seq[RichTextElement]) extends BlockElement with RichTextElement {
  override val `type`: String = "rich_text_preformatted"
}

object RichTextPreformattedElement {
  implicit val decoder: Decoder[RichTextPreformattedElement] = deriveDecoder[RichTextPreformattedElement]
}

object BlockElement {
  implicit val eitherOptFmt: Codec[Either[OptionObject, OptionGroupObject]] =
    eitherObjectFormat[OptionObject, OptionGroupObject]("text", "label")

  implicit val staticMenuElementFmt: Codec.AsObject[StaticSelectElement] = deriveCodec[StaticSelectElement]
  implicit val extMenuElementFmt: Codec.AsObject[ExternalSelectElement]  = deriveCodec[ExternalSelectElement]

  private val elemWrites: Encoder[BlockElement] = new Encoder[BlockElement] {
    def apply(element: BlockElement): Json = {
      val json = element match {
        case elem: ButtonElement                   => elem.asJson
        case elem: ImageElement                    => elem.asJson
        case elem: StaticSelectElement             => elem.asJson
        case elem: ExternalSelectElement           => elem.asJson
        case elem: UserSelectElement               => elem.asJson
        case elem: MultiUsersSelectElement         => elem.asJson
        case elem: ChannelSelectElement            => elem.asJson
        case elem: ConversationSelectElement       => elem.asJson
        case elem: MultiConversationsSelectElement => elem.asJson
        case elem: OverflowElement                 => elem.asJson
        case elem: DatePickerElement               => elem.asJson
      }
      Json.obj("type" -> element.`type`.asJson).deepMerge(json)
    }
  }
  private val elemReads: Decoder[BlockElement]  = new Decoder[BlockElement] {

    override def apply(c: HCursor): Result[BlockElement] =
      for {
        value  <- c.downField("type").as[String]
        result <- value match {
                    case "button"                     => c.as[ButtonElement]
                    case "image"                      => c.as[ImageElement]
                    case "static_select"              => c.as[StaticSelectElement]
                    case "external_select"            => c.as[ExternalSelectElement]
                    case "users_select"               => c.as[UserSelectElement]
                    case "multi_users_select"         => c.as[MultiUsersSelectElement]
                    case "conversations_select"       => c.as[ConversationSelectElement]
                    case "multi_conversations_select" => c.as[MultiConversationsSelectElement]
                    case "channels_select"            => c.as[ChannelSelectElement]
                    case "overflow"                   => c.as[OverflowElement]
                    case "datepicker"                 => c.as[DatePickerElement]
                    case "rich_text_section"          => c.as[RichTextSectionElement]
                    case other                        => Left(DecodingFailure(s"Invalid element type: $other", List.empty))
                  }
      } yield result
  }

  implicit val codec: Codec[BlockElement] = Codec.from(elemReads, elemWrites)
}

object InputBlockElement {

  implicit val encoder: Encoder[InputBlockElement] = Encoder.AsObject.instance[InputBlockElement] { ibe =>
    val json = ibe match {
      case i: PlainTextInput                  => i.asJsonObject
      case i: StaticSelectElement             => i.asJsonObject
      case i: DatePickerElement               => i.asJsonObject
      case i: ConversationSelectElement       => i.asJsonObject
      case i: MultiConversationsSelectElement => i.asJsonObject
      case i: UserSelectElement               => i.asJsonObject
      case i: MultiUsersSelectElement         => i.asJsonObject
    }

    json.add("type", ibe.`type`.asJson)
  }

  val typeDecoder: Decoder[String] = Decoder.decodeString.at("type")

  implicit val decoder: Decoder[InputBlockElement] = Decoder.instance[InputBlockElement] { c =>
    typeDecoder(c).flatMap {
      case "plain_text_input"           => c.as[PlainTextInput]
      case "static_select"              => c.as[StaticSelectElement]
      case "datepicker"                 => c.as[DatePickerElement]
      case "conversations_select"       => c.as[ConversationSelectElement]
      case "multi_conversations_select" => c.as[MultiConversationsSelectElement]
      case "users_select"               => c.as[UserSelectElement]
      case "multi_users_select"         => c.as[MultiUsersSelectElement]
      case t                            => Left(DecodingFailure(s"Unknown input block element type $t", c.history))
    }
  }

}

object Block {

  implicit val eitherContextFmt: Codec[Either[ImageElement, TextObject]] =
    eitherObjectFormat[ImageElement, TextObject]("image_url", "text")
  implicit val actionBlockFmt: Codec.AsObject[ActionsBlock]              = deriveCodec[ActionsBlock]
  implicit val contextBlockFmt: Codec.AsObject[ContextBlock]             = deriveCodec[ContextBlock]
  implicit val sectionFmt: Codec.AsObject[Section]                       = deriveCodec[Section]
  implicit val headerBlockCodec: Codec.AsObject[HeaderBlock]             = deriveCodec[HeaderBlock]
  implicit val inputBlockCodec: Codec.AsObject[InputBlock]               = deriveCodec[InputBlock]
  implicit val richTextBlock: Codec.AsObject[RichTextBlock]              = deriveCodec[RichTextBlock]

  private lazy val blockEncoder = Encoder.AsObject.instance[Block] { block =>
    val json = block match {
      case b: Divider       => b.asJsonObject
      case b: Section       => b.asJsonObject
      case b: ImageBlock    => b.asJsonObject
      case b: ActionsBlock  => b.asJsonObject
      case b: ContextBlock  => b.asJsonObject
      case b: HeaderBlock   => b.asJsonObject
      case b: InputBlock    => b.asJsonObject
      case b: RichTextBlock => b.asJsonObject
    }
    json.add("type", block.`type`.asJson)
  }

  private lazy val blockDecoder = Decoder.instance[Block] { c =>
    for {
      value  <- c.downField("type").as[String]
      result <- value match {
                  case "divider"   => c.as[Divider]
                  case "image"     => c.as[ImageBlock]
                  case "actions"   => c.as[ActionsBlock]
                  case "context"   => c.as[ContextBlock]
                  case "header"    => c.as[HeaderBlock]
                  case "input"     => c.as[InputBlock]
                  case "section"   => c.as[Section]
                  case "rich_text" => c.as[RichTextBlock]
                  case other       => Left(DecodingFailure(s"Invalid block type: $other", List.empty))
                }
    } yield result
  }

  implicit lazy val codec: Codec.AsObject[Block] = Codec.AsObject.from(blockDecoder, blockEncoder)
}
