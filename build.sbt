name := "playing"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc,
  javaCore,
  javaJdbc,
  javaJpa,
  cache,
  "org.pegdown"               % "pegdown"                 % "1.4.2",
  "org.parboiled"             % "parboiled-java"          % "1.1.6",
  "postgresql"                % "postgresql"              % "9.1-901-1.jdbc4",
  "org.hibernate"             % "hibernate-core"          % "4.2.3.Final",
  "org.hibernate"             % "hibernate-entitymanager" % "4.2.3.Final",
  "javax.persistence"         % "persistence-api"         % "1.0",
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
  "io.spray"                  %% "spray-json"             % "1.2.5"
)

play.Project.playScalaSettings
