import play.Project._

import com.github.play2war.plugin._

name := "playing"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.github.play2war.ext"   %% "redirect-playlogger"     % "1.0.1",
  "org.webjars"               %% "webjars-play"            % "2.2.1-2",
  "org.webjars"               %  "bootstrap"               % "3.1.1",
  "org.webjars"               %  "jquery"                  % "2.1.0-2",
  "org.webjars"               %  "d3js"                    % "3.4.1",
  "org.webjars"               %  "jsplumb"                 % "1.5.5",
  "org.webjars"               %  "dojo"                    % "1.9.2",
  "org.webjars"               %  "font-awesome"            % "4.0.3",
  "org.webjars"               %  "highcharts"              % "3.0.9"
)

// When deploying on Wildfly, for logging, add this library dependency -
//   "com.github.play2war.ext"   %% "redirect-playlogger"     % "1.0.1",

playScalaSettings

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

Play2WarKeys.filteredArtifacts ++= Seq(
  ("com.google.guava", "guava"),
  ("com.google.code.findbugs", "findbugs"),
  ("com.fasterxml.jackson.core","jackson-annotations"),
  ("com.fasterxml.jackson.core","jackson-core"),
  ("com.fasterxml.jackson.core","jackson-databind"),
  ("com.fasterxml","classmate"),
  ("commons-codec","commons-codec"),
  ("commons-io","commons-io"),
  ("org.hibernate","hibernate-commons-annotations"),
  ("org.hibernate","hibernate-core"),
  ("org.hibernate","hibernate-entitymanager"),
  ("org.hibernate","hibernate-validator"),
  ("org.hibernate.common","hibernate-commons-annotations"),
  ("org.hibernate.javax.persistence","hibernate-jpa-2.0-api"),
  ("javax.validation","validation-api"),
  ("javax.persistence","persistence-api"),
  ("javax.transaction","transaction-api"),
  ("org.jboss.spec.javax.transaction","jboss-transaction-api_1.1_spec"),
  ("org.jboss.logging","jboss-logging"),
  ("org.jboss.logmanager", "log4j-jboss-logmanager"),
  ("org.springframework","spring-beans"),
  ("org.springframework","spring-context"),
  ("org.springframework","spring-core"),
  ("postgresql","postgresql"),
  ("org.javassist","javassist"),
  ("org.yaml","snakeyaml"),
  ("antlr","antlr"),
  ("com.h2database","h2"),
  ("dom4j","dom4j"),
  ("tyrex","tyrex")
  //("com.jolbox", "bonecp"),
  //("io.netty","netty"),
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

