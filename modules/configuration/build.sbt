import play.Project._

name := "configuration"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.typesafe.slick"        %% "slick"                   % "2.0.0",
  "com.typesafe.play"         %% "play-slick"              % "0.6.0.1",
  "org.postgresql"            %  "postgresql"              % "9.3-1100-jdbc4",
  "joda-time"                 %  "joda-time"               % "2.3",
  "org.joda"                  %  "joda-convert"            % "1.5",
  "com.github.tototoshi"      %% "slick-joda-mapper"       % "1.0.1",
  "ws.securesocial"           %% "securesocial"            % "2.1.3"
)

playScalaSettings

exportJars := true