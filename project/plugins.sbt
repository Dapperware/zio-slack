resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8.1")
addSbtPlugin("com.jsuereth"   % "sbt-pgp"      % "1.1.2")

//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
