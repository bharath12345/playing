import play.Project._

name := "configuration"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc,
  javaCore,
  javaJdbc,
  javaJpa,
  cache,
  "postgresql"                % "postgresql"              % "9.1-901-1.jdbc4",
  "org.hibernate"             % "hibernate-core"          % "4.2.3.Final",
  "org.hibernate"             % "hibernate-entitymanager" % "4.2.3.Final",
  "javax.persistence"         % "persistence-api"         % "1.0"
)

playScalaSettings

exportJars := true