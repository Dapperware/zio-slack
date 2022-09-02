package com.github.dapperware.slack

import io.circe.Decoder
import sttp.client3.SttpBackend
import zio.{ Tag, Task, Trace, UIO, URIO, ZIO, ZLayer }

trait SlackApiBase {
  def client: SlackClient

  def apiCall[T](request: Request[T, Unit])(implicit trace: Trace): UIO[SlackResponse[T]] =
    client.apiCall(request)

  def apiCall[T, A](
    request: Request[T, A]
  )(implicit ev: HasAuth[A], tag: Tag[A], trace: Trace): URIO[A, SlackResponse[T]] =
    client.apiCall(request)
}

/**
 * The root trait for the Slack API.
 */
trait Slack
    extends SlackApiBase
    with Apps
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
    with Views

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

  def request(name: String): Request[Unit, Unit] = Request.make(MethodName(name))

  def request[A: Decoder](name: String, args: (String, SlackParamMagnet)*): Request[A, Unit] =
    Request.make(MethodName(name)).formBody(args: _*).as[A]

  def make: ZIO[SlackClient, Nothing, Slack] =
    ZIO
      .serviceWith[SlackClient](c =>
        new Slack {
          val client: SlackClient = c
        }
      )

  val http: ZLayer[SttpBackend[Task, Any], Nothing, SlackClient with Slack] =
    HttpSlack.layer >+> ZLayer(make)

}
