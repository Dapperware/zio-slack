# zio-slack
An idiomatic slack client using zio

[![Release Artifacts][Badge-SonatypeReleases]][Link-SonatypeReleases]
[![CircleCI](https://circleci.com/gh/Dapperware/zio-slack.svg?style=svg)](https://circleci.com/gh/Dapperware/zio-slack)

[Link-SonatypeReleases]: https://oss.sonatype.org/content/repositories/releases/com/github/dapperware/zio-slack-api-web_2.12/ "Sonatype Releases"
[Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.dapperware/zio-slack-api-web_2.12.svg "Sonatype Releases"


Installation
--

Add the following dependency to your project's build file

For Scala 2.12.x and 2.13.x

```scala
"com.github.dapperware" %% "zio-slack-api-web" % "1.0.0-RC1"
```

zio-slack is a library for interfacing with slack using an idiomatic and easily discoverable interface.

We define most of the methods defined here: https://api.slack.com/methods (Working toward 100% coverage!). If there is one missing, or you find a bug in how it is implemented please submit an issue or PR (There are *a lot* of methods to cover and automated coverage isn't fully available yet).

Usage
--

Usage is quite simple. First you can define how you would like to interact with slack. For instance, say you periodically wanted to send Chuck Norris jokes to random channels (...please don't).

All the APIs are contained under a single root object `Slack`.

```scala
import com.github.dapperware.Slack
```

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
  resp <- ZIO.serviceWithZIO[SttpBackend[Task, Any]](_.send(getJoke))
  joke <- IO.fromEither(resp.body) >>= IO.fromEither
  _    <- Slack.postChatMessage("<channel_id>", joke)
} yield ()).repeat(Schedule.fixed(3.hours))
```

In order to wire up our application we'll need to provide a couple things:

1. A `Slack` implementation (the easiest one to use is the `Slack.http` layer)
2. A token (you can get one from https://api.slack.com/apps)
3. An http implementation

That's it.

We can see how we can use this approach in a full application below:

```scala
import io.circe
import io.circe.Json
import com.github.dapperware.slack._
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio._

object JokeApp extends ZIOAppDefault {

  // Builds a request which will fetch a new chuck norris joke
  val getJokeReq: Request[Either[ResponseException[String, circe.Error], Either[DecodingFailure, String]], Any] =
    basicRequest
      .get(uri"https://api.chucknorris.io/jokes/random")
      .response(asJson[Json])
      .mapResponseRight(_.hcursor.downField("value").as[String])

  val getJoke = ZIO
    .serviceWithZIO[SttpBackend[Task, Any]](_.send(getJokeReq))
    .map(_.body)
    .absolve
    .absolve
    .mapError(SlackError.fromThrowable)
  
  override val run =
    (for {
      joke <- getJoke                    // Gets a new joke
      _    <- Slack.postChatMessage("<your-channel-id>", joke)  // Sends the joke to the channel of your choice
      } yield ())
       .repeat(Schedule.fixed(3.hours)) // Repeat every three hours
       .provide(
         botToken,
         Slack.http,
         AsyncHttpClientZioBackend.layer(),
         ZLayer.succeed(AccessToken("xoxb-<your-token>"))  // The access token for your bot if you will be using slack globally
       )
}
```

All API methods will return a `SlackResponse[A]` where the `A` is the body of the value returned by the API. It will also contain
any errors or warnings that the API call returned. It also contains helpful methods to turn it into an `Either` or to convert a
`Throwable` into it for improve composition.

Realtime
--

If you have used zio-slack prior to 1.0.0 then you may have used the realtime API. That has been removed as Slack has [deprecated it](https://api.slack.com/rtm). Instead, you can now use the `Socket mode` which provides a similar experience.


Methods
--

I have done my best to include as many APIs as possible, however it is a challenging task for parttime work as there are well over 100 different APIs
If you find one missing or buggy, please submit an issue or PR.




