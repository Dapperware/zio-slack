# zio-slack
An idiomatic slack client using zio

[![Release Artifacts][Badge-SonatypeReleases]][Link-SonatypeReleases]
[![CircleCI](https://circleci.com/gh/Dapperware/zio-slack.svg?style=svg)](https://circleci.com/gh/Dapperware/zio-slack)

[Link-SonatypeReleases]: https://oss.sonatype.org/content/repositories/releases/com/github/dapperware/zio-slack-api-web_2.12/ "Sonatype Releases"
[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.dapperware/zio-slack-api-web_2.12.svg "Sonatype Releases"


Installation
--

Add the following dependency to your project's build file

For Scala 2.12.x, 2.13.x and 3.0.0

```scala
"com.github.dapperware" %% "zio-slack-api-web" % "0.9.5"
```

zio-slack is a library for interfacing with slack using an idiomatic and easily discoverable interface.

We define most of the methods defined here: https://api.slack.com/methods (Working toward 100% coverage!). If there is one missing, or you find a bug in how it is implemented please submit an issue or PR (There are *a lot* of methods to cover and automated coverage isn't fully available yet).

Usage
--

Usage is quite simple. First you can define how you would like to interact with slack. For instance, say you periodically wanted to send Chuck Norris jokes to random channels (...please don't).

Since we will be sending messages we will be using the `chats` api which we can import like so:

```scala
import com.github.dapperware.slack.api.chats._
```

Note that you can pull in the functionality piecemeal like above or all at once using `slack.api.web._` you can even pull in individual methods! The beauty of ZIO is that all the methods just return effects so there is no need to instantiate anything until you are ready to execute it.

We'll use the STTP library since it is what zio-slack is built with. First lets makes a small request to fetch chuck norris jokes:

```scala
  val getJoke: Request[Either[ResponseError[circe.Error], String], Nothing] = basicRequest
    .get(uri"https://api.chucknorris.io/jokes/random")
    .response(asJson[Json])
    .mapResponseRight(_.hcursor.downField("value").as[String])
```

Next lets build a small program that would send these jokes to our slack workspace:

```scala
(for {
  resp <- SttpClient.send(getJoke)
  joke <- IO.fromEither(resp.body) >>= IO.fromEither
  _    <- postChatMessage("<channel_id>", joke)
} yield ()).repeat(Schedule.fixed(3.hours))
```

Almost there. 

You may have noticed that this is now returning compiler errors saying that it expects a `Clock with SlackEnvDefinition.this.SlackEnv` this is expected, ZIO is telling us that we need to supply our Slack environment in order to run, lets add that now, for completeness we'll throw the whole thing into a `ManagedApp`:

```scala
import io.circe
import io.circe.Json
import com.github.dapperware.slack.api.chats._
import com.github.dapperware.slack.access.AccessToken
import com.github.dapperware.slack.core.client.SlackClient
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio.duration._
import zio.{ IO, App, Schedule, ExitCode }

object JokeApp extends App {

  // Builds a request which will fetch a new chuck norris joke
  val getJoke: Request[Either[ResponseError[circe.Error], Either[DecodingFailure, String]], Nothing] = basicRequest
    .get(uri"https://api.chucknorris.io/jokes/random")
    .response(asJson[Json])
    .mapResponseRight(_.hcursor.downField("value").as[String])
    
  // Creates our static environmental layer
  val envLayer = AsyncHttpClientZioBackend.layer() >>> SlackClient.live

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    (for {
      resp <- SttpClient.send(getJoke)                    // Gets a new joke
      joke <- IO.fromEither(resp.body) >>= IO.fromEither  // Decodes the joke response
      _    <- postChatMessage("<your-channel-id>", joke)  // Sends the joke to the channel of your choice
      } yield ())
       .repeat(Schedule.fixed(3.hours)) // Repeat every three hours
       .provideCustomLayer(envLayer ++ AccessToken.make("xoxb-<your-token>").toLayer) // Add the token used to authorize requests to slack
       .exitCode
}
```

Caveat
--

With Scala 3.0.0, you might encounter a run time error similar to
```
Fiber failed.
An unchecked error was produced.
java.lang.Error: Defect in zio.Has: Set({{Has[=Token] & Has[=package$::SlackClient$::Service]} & Has[=package$::SlackRealtimeClient$::Service]}) statically known to be contained within the environment are missing
	at zio.Has$HasSyntax$.prune$extension(Has.scala:198)
	at zio.Has$HasSyntax$.union$extension(Has.scala:210)
	at zio.Has$$anon$2.union(Has.scala:92)
	at zio.Has$$anon$2.union(Has.scala:91)

```
The workaround for this is to use the environments explictely instead of using the type aliases. Replace `AccessToken` with `Has[Token]`, `ClientSecret` with `Has[ClientSecretToken]` and `SlackRealtimeClient` with `Has[SlackRealtimeClient.Service]`. The programs in the `examples` module compile and run with Scala 3.0.0.
