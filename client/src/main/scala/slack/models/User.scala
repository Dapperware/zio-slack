package slack.models

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

case class UserProfile(
  first_name: Option[String] = None,
  last_name: Option[String] = None,
  real_name: Option[String] = None,
  email: Option[String] = None,
  skype: Option[String] = None,
  phone: Option[String] = None,
  image_24: String,
  image_32: String,
  image_48: String,
  image_72: String,
  image_192: String
)
