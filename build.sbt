organization := "com.github.nivox"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

crossScalaVersions := Seq("2.10.6")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "io.argonaut" %% "argonaut" % "6.0.4",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
