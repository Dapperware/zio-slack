resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")

addSbtPlugin("org.xerial.sbt"      % "sbt-sonatype"       % "3.8.1")
addSbtPlugin("com.jsuereth"        % "sbt-pgp"            % "2.0.1")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.2.2")

//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
