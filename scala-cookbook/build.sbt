import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("ch01to12"))
  .settings(
    name := "ch01to12",
    libraryDependencies += scalaTest % Test
  )

lazy val ch1301 = (project in file("ch1301"))
  .settings(
    name := "ch1301",
    libraryDependencies += akka
  )

lazy val ch1302 = (project in file("ch1302"))
  .settings(
    name := "ch1302",
    libraryDependencies += akka
  )

lazy val ch1303 = (project in file("ch1303"))
  .settings(
    name := "ch1303",
    libraryDependencies += akka
  )

lazy val ch1304 = (project in file("ch1304"))
  .settings(
    name := "ch1304",
    libraryDependencies += akka
  )

lazy val ch1305 = (project in file("ch1305"))
  .settings(
    name := "ch1305",
    libraryDependencies += akka
  )

lazy val ch1306 = (project in file("ch1306"))
  .settings(
    name := "ch1306",
    libraryDependencies += akka
  )

lazy val ch1308 = (project in file("ch1308"))
  .settings(
    name := "ch1308",
    libraryDependencies += akka
  )

lazy val ch1309 = (project in file("ch1309"))
  .settings(
    name := "ch1309",
    libraryDependencies += akka
  )

lazy val ch1310 = (project in file("ch1310"))
  .settings(
    name := "ch1310",
    libraryDependencies += akka
  )

lazy val ch1311 = (project in file("ch1311"))
  .settings(
    name := "ch1311",
    libraryDependencies += akka
  )

lazy val ch1312 = (project in file("ch1312"))
  .settings(
    name := "ch1312",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += scalaParallelCollections,
  )

lazy val mongotour = (project in file("mongo-tour"))
  .settings(
    name := "mongotour",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += mongoDriver,
  )

lazy val ch19 = (project in file("ch19"))
  .settings(
    name := "ch19",
    libraryDependencies += scalaTest % Test,
  )
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
