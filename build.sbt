import play.Project._

name := "playing"

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
)

playScalaSettings

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

