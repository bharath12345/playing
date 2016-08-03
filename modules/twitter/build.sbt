import play.Project._

name := "twitter"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Scribe" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo"

resolvers += "TempDB" at "http://maven.tempo-db.com/artifactory/repo"

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
  "com.typesafe"              %% "play-plugins-mailer"    % "2.1-RC2",
  "org.scribe"                %  "scribe"                 % "1.3.5",
  "ws.securesocial"           %% "securesocial"           % "2.1.3",
  "com.typesafe.slick"        %% "slick"                  % "2.0.0",
  "com.typesafe.play"         %% "play-slick"             % "0.6.0.1",
  "org.postgresql"            %  "postgresql"             % "9.3-1100-jdbc4",
  "joda-time"                 %  "joda-time"              % "2.3",
  "org.joda"                  %  "joda-convert"           % "1.5",
  "com.github.tototoshi"      %% "slick-joda-mapper"      % "1.0.1",
  "com.tempodb"               %  "tempodb-java"           % "0.7.1",
  "org.apache.kafka"          % "kafka_2.10"              % "0.8.1" excludeAll (
    ExclusionRule(organization = "com.sun.jdmk"),
    ExclusionRule(organization = "com.sun.jmx"),
    ExclusionRule(organization = "javax.jms"),
    ExclusionRule(organization = "org.slf4j")
    )
)

playScalaSettings

routesImport ++= Seq("language.reflectiveCalls")

exportJars := true