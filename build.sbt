name := """json-schema-validator-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers += "EclipseSource Repository" at "https://dl.bintray.com/emueller/maven/"

//resolvers += "EclipseSource Repository" at "http://localhost:8080"

libraryDependencies += "com.eclipsesource"  %% "play-json-schema-validator" % "0.8.6"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.5.2" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-ace" % "0.2.3"
)


