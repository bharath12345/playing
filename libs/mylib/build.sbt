name := "mylib"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  "javax"                        % "javaee-api"             % "7.0",
  "org.jboss.logmanager"         % "log4j-jboss-logmanager" % "1.0.0.Final"
)

//"org.glassfish.javaeetutorial" % "helloservice-api"       % "7.0.4-SNAPSHOT",

exportJars := true