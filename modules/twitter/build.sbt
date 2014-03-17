import play.Project._

name := "twitter"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Scribe" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo"

libraryDependencies ++= Seq(
  "oauth.signpost"            % "signpost-core"           % "1.2",
  "oauth.signpost"            % "signpost-commonshttp4"   % "1.2",
  "org.apache.httpcomponents" % "httpclient"              % "4.3.2",
  "org.apache.commons"        % "commons-io"              % "1.3.2",
  "org.apache.httpcomponents" % "fluent-hc"               % "4.3.2",
  "com.typesafe.akka"         %% "akka-actor"             % "2.2.3",
  "com.typesafe.akka"         %% "akka-slf4j"             % "2.2.3",
  "io.spray"                  % "spray-can"               % "1.2.0",
  "io.spray"                  % "spray-client"            % "1.2.0",
  "io.spray"                  % "spray-routing"           % "1.2.0",
  "io.spray"                  %% "spray-json"             % "1.2.5",
  "org.webjars"               %% "webjars-play"           % "2.2.1-2",
  "org.webjars"               %  "bootstrap"              % "3.1.1",
  "org.webjars"               %  "jquery"                 % "2.1.0-2",
  "org.webjars"               %  "d3js"                   % "3.4.1",
  "org.webjars"               %  "jsplumb"                % "1.5.5",
  "org.webjars"               %  "dojo"                   % "1.9.2",
  "org.webjars"               %  "font-awesome"           % "4.0.3",
  "org.webjars"               %  "highcharts"             % "3.0.9",
  "com.typesafe"              %% "play-plugins-mailer"    % "2.1-RC2",
  "org.scribe"                %  "scribe"                 % "1.3.5"
)

playScalaSettings

exportJars := true