resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")

addSbtPlugin("org.xerial.sbt"      % "sbt-sonatype"       % "3.9.7")
addSbtPlugin("com.jsuereth"        % "sbt-pgp"            % "1.1.2")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.4.0")

//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
