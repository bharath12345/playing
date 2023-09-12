name := """playing"""

organization := "in.bharathwrites"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
  )

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.13.12"

libraryDependencies += guice

libraryDependencies ++= Seq(
  "org.pegdown" % "pegdown" % "1.4.2",
  "org.parboiled" % "parboiled-java" % "1.1.6",
  "org.elasticsearch" % "elasticsearch" % "1.1.0",
  "com.sksamuel.elastic4s" % "elastic4s_2.10" % "1.0.1.1",
  "com.netflix.rxjava" % "rxjava-core" % "0.17.5",
  "io.searchbox" % "jest" % "0.1.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
)

addCommandAlias(
  "validateCode",
  "scalafmtSbtCheck; scalafmtCheckAll; uiCodeStyleCheck"
)
