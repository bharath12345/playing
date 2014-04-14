import play.Project._

name := "blog"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-feature")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Sonatype groups" at "https://oss.sonatype.org/content/groups/public/"

libraryDependencies ++= Seq(
  "org.pegdown"               %  "pegdown"                 % "1.4.2",
  "org.parboiled"             %  "parboiled-java"          % "1.1.6",
  "org.elasticsearch"         %  "elasticsearch"           % "1.1.0",
  "com.sksamuel.elastic4s"    %  "elastic4s_2.10"          % "1.0.1.1",
  "com.netflix.rxjava"        %  "rxjava-core"             % "0.17.5",
  "org.scala-lang.modules"    %% "scala-async"             % "0.9.1",
  "io.searchbox"              %  "jest"                    % "0.1.0"
)

playScalaSettings

routesImport ++= Seq("language.reflectiveCalls")

exportJars := true