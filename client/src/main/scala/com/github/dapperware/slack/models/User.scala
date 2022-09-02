package com.github.dapperware.slack.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class User(
  id: String,
  name: String,
  deleted: Option[Boolean] = None,
  color: Option[String] = None,
  profile: Option[UserProfile] = None,
  is_bot: Option[Boolean] = None,
  is_admin: Option[Boolean] = None,
  is_owner: Option[Boolean] = None,
  is_primary_owner: Option[Boolean] = None,
  is_restricted: Option[Boolean] = None,
  is_ultra_restricted: Option[Boolean] = None,
  has_2fa: Option[Boolean] = None,
  has_files: Option[Boolean] = None,
  tz: Option[String] = None,
  tz_offset: Option[Int] = None,
  presence: Option[String] = None
)

object User {
  implicit val codec: Codec.AsObject[User] = deriveCodec[User]
}

case class UserProfile(
  real_name: String,
  real_name_normalized: String,
  display_name: String,
  display_name_normalized: String,
  status_text: String,
  status_emoji: String,
  status_expiration: Option[Int] = None,
  avatar_hash: String,
  user_id: Option[String] = None,
  first_name: Option[String] = None,
  last_name: Option[String] = None,
  email: Option[String] = None,
  skype: Option[String] = None,
  phone: Option[String] = None,
  title: Option[String] = None,
  image_24: Option[String] = None,
  image_32: Option[String] = None,
  image_48: Option[String] = None,
  image_72: Option[String] = None,
  image_192: Option[String] = None,
  image_512: Option[String] = None,
  image_1024: Option[String] = None,
  image_original: Option[String] = None,
  guest_invited_by: Option[String] = None,
  guest_expiration_ts: Option[Int] = None,
  bot_id: Option[String] = None,
  always_active: Option[Boolean] = None,
  api_app_id: Option[String] = None,
  pronouns: Option[String] = None
)

object UserProfile {
  implicit val codec: Codec.AsObject[UserProfile] = deriveCodec[UserProfile]
}
