name := """json-schema-validator-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

resolvers += "EclipseSource Repository" at "https://dl.bintray.com/emueller/maven/"

libraryDependencies += "com.eclipsesource"  %% "play-json-schema-validator" % "0.9.5"

libraryDependencies += guice

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.6.3",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.6.8" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-ace" % "0.2.3"
)

