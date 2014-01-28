name := "playing"

version := "1.0-SNAPSHOT"

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
  "org.apache.httpcomponents" % "fluent-hc"               % "4.3.2"
)

play.Project.playScalaSettings
