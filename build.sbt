import play.Project._

import com.github.play2war.plugin._

name := "playing"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.pegdown"               % "pegdown"                 % "1.4.2",
  "org.parboiled"             % "parboiled-java"          % "1.1.6",
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
  "com.github.play2war.ext"   %% "redirect-playlogger"    % "1.0.1"
)

playScalaSettings

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

Play2WarKeys.filteredArtifacts ++= Seq(
  ("com.google.guava", "guava"),
  ("com.fasterxml.jackson.core","jackson-annotations"),
  ("com.fasterxml.jackson.core","jackson-core"),
  ("com.fasterxml.jackson.core","jackson-databind"),
  ("com.h2database","h2"),
  ("commons-codec","commons-codec"),
  ("commons-io","commons-io"),
  //("io.netty","netty"),
  ("org.hibernate","hibernate-commons-annotations"),
  ("org.hibernate","hibernate-core"),
  ("org.hibernate","hibernate-entitymanager"),
  ("org.hibernate","hibernate-validator"),
  ("org.javassist","javassist"),
  ("org.jboss.logging","jboss-logging"),
  ("javax.transaction","transaction-api"),
  ("org.yaml","snakeyaml"),
  ("antlr","antlr"),
  ("dom4j","dom4j"),
  ("postgresql","postgresql"),
  ("javax.validation","validation-api")
)

lazy val playing = project.in( file(".") )
                       .aggregate(twitter)
                       .dependsOn(twitter)
                       //.aggregate(configuration)
                       //.dependsOn(configuration)

lazy val twitter = project

lazy val configuration = project.in(file("modules/configuration"))

