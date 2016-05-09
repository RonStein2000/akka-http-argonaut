organization := "io.github.nivox"

name := "akka-http-argonaut"

description := """Akka HTTP bindings to argonaut.io for Marshalling/Unmarshalling"""

homepage := Some(url("https://github.com/nivox/akka-http-argonaut"))

startYear := Some(2015)

version := "0.2"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

crossScalaVersions := Seq("2.11.7", "2.10.6")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4" % "provided",
  "io.argonaut" %% "argonaut" % "6.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
