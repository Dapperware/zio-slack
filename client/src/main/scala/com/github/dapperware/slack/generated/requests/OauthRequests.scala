/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param code The `code` param returned via the OAuth callback.
 * @param redirect_uri This must match the originally submitted URI (if one was sent).
 * @param single_channel Request the user to add your app only to a single channel. Only valid with a [legacy workspace app](https://api.slack.com/legacy-workspace-apps).
 */
case class AccessOauthRequest(code: Option[String], redirect_uri: Option[String], single_channel: Option[Boolean])

object AccessOauthRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AccessOauthRequest] = FormEncoder.fromParams.contramap[AccessOauthRequest] { req =>
    List("code" -> req.code, "redirect_uri" -> req.redirect_uri, "single_channel" -> req.single_channel)
  }
}

/**
 * @param code The `code` param returned via the OAuth callback.
 * @param redirect_uri This must match the originally submitted URI (if one was sent).
 * @param single_channel Request the user to add your app only to a single channel.
 */
case class TokenOauthRequest(code: Option[String], redirect_uri: Option[String], single_channel: Option[Boolean])

object TokenOauthRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[TokenOauthRequest] = FormEncoder.fromParams.contramap[TokenOauthRequest] { req =>
    List("code" -> req.code, "redirect_uri" -> req.redirect_uri, "single_channel" -> req.single_channel)
  }
}

/**
 * @param code The `code` param returned via the OAuth callback.
 * @param redirect_uri This must match the originally submitted URI (if one was sent).
 */
case class AccessV2OauthRequest(code: String, redirect_uri: Option[String])

object AccessV2OauthRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[AccessV2OauthRequest] = FormEncoder.fromParams.contramap[AccessV2OauthRequest] {
    req =>
      List("code" -> req.code, "redirect_uri" -> req.redirect_uri)
  }
}
