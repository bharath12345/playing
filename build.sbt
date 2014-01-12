name := "playing"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.pegdown"            %   "pegdown"            % "1.4.2",
  "org.parboiled"          %   "parboiled-java"     % "1.1.6"
)

play.Project.playScalaSettings
