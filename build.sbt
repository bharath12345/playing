name := """playing"""

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.webjars"               %% "webjars-play"            % "2.8.8-1",
  "org.webjars"               %  "bootstrap"               % "3.1.1",
  "org.webjars"               %  "dojo"                    % "1.9.2"
)

lazy val playing = (project in file("."))
  .aggregate(blog, react)
  .dependsOn(blog, react)
  .enablePlugins(PlayScala)

lazy val blog = (project in file("modules/blog")).enablePlugins(PlayScala)

lazy val react = (project in file("modules/react")).enablePlugins(PlayScala)

import com.typesafe.sbt.packager.MappingsHelper._

mappings in Universal ++= directory(baseDirectory.value / "modules" / "blog" / "posts")
