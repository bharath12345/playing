import play.Project._

name := "blog"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.pegdown"               %  "pegdown"                 % "1.4.2",
  "org.parboiled"             %  "parboiled-java"          % "1.1.6",
  "org.webjars"               %% "webjars-play"            % "2.2.1-2",
  "org.webjars"               %  "bootstrap"               % "3.1.1",
  "org.webjars"               %  "jquery"                  % "2.1.0-2",
  "org.webjars"               %  "d3js"                    % "3.4.1",
  "org.webjars"               %  "jsplumb"                 % "1.5.5",
  "org.webjars"               %  "dojo"                    % "1.9.2",
  "org.webjars"               % "font-awesome"             % "4.0.3"
)

playScalaSettings

exportJars := true