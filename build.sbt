name := "playing"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.pegdown"               % "pegdown"                 % "1.4.2",
  "org.parboiled"             % "parboiled-java"          % "1.1.6",
  "postgresql"                % "postgresql"              % "9.1-901-1.jdbc4",
  "org.hibernate"             % "hibernate-core"          % "4.2.3.Final",
  "org.hibernate"             % "hibernate-entitymanager" % "4.2.3.Final"
)

play.Project.playScalaSettings
