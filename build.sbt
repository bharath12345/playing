import play.Project._

name := "playing"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.webjars"               %% "webjars-play"            % "2.2.1-2",
  "org.webjars"               %  "bootstrap"               % "3.1.1",
  "org.webjars"               %  "jquery"                  % "2.1.0-2",
  "org.webjars"               %  "d3js"                    % "3.4.1",
  "org.webjars"               %  "jsplumb"                 % "1.5.5",
  "org.webjars"               %  "font-awesome"            % "4.0.3",
  "org.webjars"               %  "dojo"                    % "1.9.2",
  "org.webjars"               %  "highcharts"             % "3.0.9"
)

playScalaSettings

routesImport ++= Seq("language.reflectiveCalls")

lazy val playing = project.in( file(".") )
                       //.aggregate(mylib)
                       //.dependsOn(mylib)
                       //.aggregate(configuration)
                       //.dependsOn(configuration)
                       .aggregate(blog)
                       .dependsOn(blog)
                       //.aggregate(twitter)
                       //.dependsOn(twitter)

// Library JARs
//lazy val mylib = project.in(file("libs/mylib"))

// Play Sub-Modules
//lazy val configuration = project.in(file("modules/configuration"))

lazy val blog = project.in(file("modules/blog"))

//lazy val twitter = project.in(file("modules/twitter"))



