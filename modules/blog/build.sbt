import play.Project._

name := "blog"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.pegdown"               %  "pegdown"                 % "1.4.2",
  "org.parboiled"             %  "parboiled-java"          % "1.1.6",
  "com.sksamuel.elastic4s"    % "elastic4s_2.10"           % "1.0.1.1"
)

playScalaSettings

exportJars := true