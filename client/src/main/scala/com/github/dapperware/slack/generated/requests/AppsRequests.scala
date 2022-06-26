/* This file was automatically generated update at your own risk */

package com.github.dapperware.slack.generated.requests

/**
 * @param event_context undefined
 * @param cursor undefined
 * @param limit undefined
 */
case class ListAuthorizationsEventAppsRequest(event_context: String, cursor: Option[String], limit: Option[Int])

object ListAuthorizationsEventAppsRequest {
  import io.circe.generic.semiauto.deriveEncoder
  import io.circe.Encoder
  implicit val encoder: Encoder[ListAuthorizationsEventAppsRequest] = deriveEncoder[ListAuthorizationsEventAppsRequest]
}

/**
 * @param scopes A comma separated list of scopes to request for
 * @param trigger_id Token used to trigger the permissions API
 */
case class RequestPermissionsAppsRequest(scopes: String, trigger_id: String)

object RequestPermissionsAppsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[RequestPermissionsAppsRequest] =
    FormEncoder.fromParams.contramap[RequestPermissionsAppsRequest] { req =>
      List("scopes" -> req.scopes, "trigger_id" -> req.trigger_id)
    }
}

/**
 * @param cursor Paginate through collections of data by setting the `cursor` parameter to a `next_cursor` attribute returned by a previous request's `response_metadata`. Default value fetches the first "page" of the collection. See [pagination](/docs/pagination) for more detail.
 * @param limit The maximum number of items to return.
 */
case class ListResourcesPermissionsAppsRequest(cursor: Option[String], limit: Option[Int])

object ListResourcesPermissionsAppsRequest {
  import com.github.dapperware.slack.FormEncoder
  implicit val encoder: FormEncoder[ListResourcesPermissionsAppsRequest] =
    FormEncoder.fromParams.contramap[ListResourcesPermissionsAppsRequest] { req =>
      List("cursor" -> req.cursor, "limit" -> req.limit)
    }
}
