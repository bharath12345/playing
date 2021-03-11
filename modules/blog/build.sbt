name := "blog"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.1"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  "org.pegdown"               %  "pegdown"                 % "1.4.2",
  "org.parboiled"             %  "parboiled-java"          % "1.1.6",
  "org.elasticsearch"         %  "elasticsearch"           % "1.1.0",
  "com.sksamuel.elastic4s"    %  "elastic4s_2.10"          % "1.0.1.1",
  "com.netflix.rxjava"        %  "rxjava-core"             % "0.17.6",
  "io.searchbox"              %  "jest"                    % "0.1.0"
)

exportJars := true