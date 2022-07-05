resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")

addSbtPlugin("org.xerial.sbt"      % "sbt-sonatype"       % "3.9.2")
addSbtPlugin("com.jsuereth"        % "sbt-pgp"            % "1.1.2")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.6.0")
//addSbtPlugin("com.47deg"           % "sbt-microsites"     % "1.3.4")

//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
