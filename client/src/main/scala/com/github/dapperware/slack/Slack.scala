package com.github.dapperware.slack

import io.circe.Decoder
import sttp.client3.asynchttpclient.zio.SttpClient
import sttp.client3.{ Identity, RequestT }
import zio.{ Has, Tag, UIO, URIO, ZIO }

/**
 * The root trait for the Slack API.
 */
trait Slack
    extends Apps
    with Auth
    with Bots
    with Calls
    with Chats
    with Conversations
    with Dialogs
    with Dnd
    with Emojis
    with Files
    with OAuth
    with Pins
    with Profiles
    with Reactions
    with Reminders
    with RemoteFiles
    with Rtm
    with Search
    with Stars
    with Teams
    with UserGroups
    with Users
    with Views {

  def apiCall[T](request: Request[T, Unit]): UIO[SlackResponse[T]]

  def apiCall[T, A](request: Request[T, A])(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[A], SlackResponse[T]]

}

object Slack
    extends AppsAccessors
    with AuthAccessors
    with BotsAccessors
    with CallsAccessors
    with ChatsAccessors
    with ConversationsAccessors
    with DialogsAccessors
    with DndAccessors
    with EmojisAccessors
    with FilesAccessors
    with OAuthAccessors
    with PinsAccessors
    with ProfilesAccessors
    with ReactionsAccessors
    with RemindersAccessors
    with RemoteFilesAccessors
    with RtmAccessors
    with SearchAccessors
    with StarsAccessors
    with TeamsAccessors
    with UserGroupsAccessors
    with UsersAccessors
    with ViewsAccessors {

  def apiCall[T](request: Request[T, Unit]): URIO[Has[Slack], SlackResponse[T]] =
    ZIO.serviceWith(_.apiCall(request))

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[Slack] with Has[A], SlackResponse[T]] =
    ZIO.accessM(_.get[Slack].apiCall[T, A](request))

  def request(name: String): Request[Unit, AccessToken] = Request.make(MethodName(name))

  def request[A: Decoder](name: String, args: (String, SlackParamMagnet)*): Request[A, AccessToken] =
    Request.make(MethodName(name)).formBody(args: _*).as[A]

}

class HttpSlack private (baseUrl: String, client: SttpClient.Service) extends Slack {

  private def makeCall[A](request: RequestT[Identity, SlackResponse[A], Any]): URIO[Any, SlackResponse[A]] =
    client
      .send(request)
      .mapBoth(SlackError.fromThrowable, _.body)
      .merge

  def apiCall[T](request: Request[T, Unit]): UIO[SlackResponse[T]] =
    makeCall(request.toRequest(baseUrl))

  def apiCall[T, A](request: Request[T, A])(implicit ev: HasAuth[A], tag: Tag[A]): URIO[Has[A], SlackResponse[T]] =
    ZIO.service[A].flatMap(auth => makeCall(ev(request.toRequest(baseUrl))(auth)))
}

object HttpSlack {
  final val SlackBaseUrl = "https://slack.com/api/"

  def make: ZIO[Has[SttpClient.Service], Nothing, Slack] = make(SlackBaseUrl)

  def make(url: String): ZIO[Has[SttpClient.Service], Nothing, Slack] =
    ZIO.service[SttpClient.Service].map(new HttpSlack(url, _))

  def layer = make.toLayer
}
