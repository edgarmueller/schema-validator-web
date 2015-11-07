name := """json-schema-validator-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "EclipseSource Repository" at "https://dl.bintray.com/emueller/maven/"

libraryDependencies += "com.eclipsesource"  %% "play-json-schema-validator" % "0.5.5"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.4.0",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.3.14" exclude("org.webjars", "jquery"),
  // ---
  "io.github.jto" %% "validation-core" % "1.1",
  "io.github.jto" %% "validation-json" % "1.1"
)

