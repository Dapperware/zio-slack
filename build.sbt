name in ThisBuild := "zio-slack"
organization in ThisBuild := "com.github.dapperware"

inThisBuild(
  developers := List(
    Developer(
      "paulpdaniels",
      "Paul Daniels",
      "",
      url("https://github.com/paulpdaniels")
    )
  )
)

version in ThisBuild := "0.1"

val mainScala = "2.12.10"
val allScala = Seq("2.13.1", mainScala)

scalaVersion := mainScala

val circeV = "0.12.3"
val zioV = "1.0.0-RC17"
val sttpV = "2.0.0-RC5"

lazy val root = (project in file("."))
  .aggregate(client, realtime, examples)
  .settings(skip in publish := true)

lazy val client = project.in(file("client"))
    .settings(name := "zio-slack-client")
    .settings(commonSettings)
    .settings(
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-generic" % circeV,
        "dev.zio" %% "zio-macros-core" % "0.6.2",
        "dev.zio" %% "zio-macros-test" % "0.6.0",
        "dev.zio" %% "zio-test" % zioV % "it,test",
        "dev.zio" %% "zio-test-sbt" % zioV % "it,test",
        "com.softwaremill.sttp.client" %% "core" % sttpV,
        "com.softwaremill.sttp.client" %% "circe" % sttpV,
        "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV
      ),
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
    )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)

lazy val realtime = project.in(file("realtime"))
  .dependsOn(client)
    .settings(name := "zio-slack-realtime")
    .settings(commonSettings)
    .settings(
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-generic" % circeV,
        "dev.zio" %% "zio-macros-core" % "0.6.2",
        "dev.zio" %% "zio-macros-test" % "0.6.0",
        "dev.zio" %% "zio-test" % zioV % "it,test",
        "dev.zio" %% "zio-test-sbt" % zioV % "it,test",
        "com.softwaremill.sttp.client" %% "core" % sttpV,
        "com.softwaremill.sttp.client" %% "circe" % sttpV,
        "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV
      ),
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
    )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)

lazy val examples = project.in(file("examples"))
    .dependsOn(client, realtime)
    .settings(commonSettings)
    .settings(
      skip in publish := true,
      libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.2"
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
    case _ => Nil
  })
)