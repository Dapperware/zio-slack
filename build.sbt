name in ThisBuild := "zio-slack"
organization in ThisBuild := "com.github.dapperware"


val mainScala = "2.12.10"
val allScala = Seq("2.13.1", mainScala)

inThisBuild(
  List(
    homepage := Some(url("https://github.com/dapperware/zio-slack")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/dapperware/zio-slack/"),
        "scm:git:git@github.com:dapperware/zio-slack.git"
      )
    ),
    developers := List(
      Developer(
        "paulpdaniels",
        "Paul Daniels",
        "",
        url("https://github.com/paulpdaniels")
      )
    ),
    crossScalaVersions := allScala,
  )
)

scalaVersion in ThisBuild := mainScala
gitVersioningSnapshotLowerBound in ThisBuild := "0.4.0"

val circeV = "0.13.0"
val zioV = "1.0.0-RC18-2"
val sttpV = "2.0.6"

publishTo in ThisBuild := sonatypePublishToBundle.value

lazy val root = (project in file("."))
  .aggregate(core, client, realtime, examples)
  .settings(skip in publish := true)
  .settings(historyPath := None)

lazy val core = project.in(file("core"))
  .settings(name := "zio-slack-core")
  .settings(commonSettings)
  .settings(
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioV,
      "dev.zio" %% "zio-test" % zioV % Test,
      "dev.zio" %% "zio-test-sbt" % zioV % Test,
      "com.softwaremill.sttp.client" %% "core" % sttpV,
      "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV
    )
  )

lazy val client = project.in(file("client"))
    .dependsOn(core)
    .settings(name := "zio-slack-api-web")
    .settings(commonSettings)
    .settings(
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-generic" % circeV,
        "dev.zio" %% "zio-test" % zioV % "it,test",
        "dev.zio" %% "zio-test-sbt" % zioV % "it,test",
        "com.softwaremill.sttp.client" %% "core" % sttpV,
        "com.softwaremill.sttp.client" %% "circe" % sttpV,
        "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV
      ),
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)

lazy val realtime = project.in(file("realtime"))
    .dependsOn(core, client)
    .settings(name := "zio-slack-api-realtime")
    .settings(commonSettings)
    .settings(
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-generic" % circeV,
        "dev.zio" %% "zio-streams" % zioV,
        "dev.zio" %% "zio-test" % zioV % "it,test",
        "dev.zio" %% "zio-test-sbt" % zioV % "it,test",
        "com.softwaremill.sttp.client" %% "core" % sttpV,
        "com.softwaremill.sttp.client" %% "circe" % sttpV,
        "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV
      ),
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)

lazy val examples = project.in(file("examples"))
    .dependsOn(client, realtime)
    .settings(commonSettings)
    .settings(
      skip in publish := true,
      libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.3"
    )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings"
//  "-Ymacro-annotations"
)


val commonSettings = Def.settings(
  libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 13)) => Nil
    case _ => compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full) :: Nil
  }),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-explaintypes",
    "-Yrangepos",
    "-feature",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps",
    "-unchecked",
    "-Xlint:_,-type-parameter-shadow",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused:patvars,-implicits",
    "-Ywarn-value-discard"
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) =>
      Seq(
        "-Xsource:2.13",
        "-Yno-adapted-args",
        "-Ypartial-unification",
        "-Ywarn-extra-implicit",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-opt-inline-from:<source>",
        "-opt-warnings",
        "-opt:l:inline"
      )
    case Some((2, 13)) =>
      Seq(
        "-Ymacro-annotations"
      )
    case _ => Nil
  })
)
