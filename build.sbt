import play.Project._

import com.github.play2war.plugin._

name := "playing"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
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
                       .aggregate(mylib)
                       .dependsOn(mylib)
                       .aggregate(configuration)
                       .dependsOn(configuration)
                       .aggregate(blog)
                       .dependsOn(blog)
                       .aggregate(twitter)
                       .dependsOn(twitter)

// Library JARs
lazy val mylib = project.in(file("libs/mylib"))

// Play Sub-Modules
lazy val configuration = project.in(file("modules/configuration"))

lazy val blog = project.in(file("modules/blog"))

lazy val twitter = project.in(file("modules/twitter"))

