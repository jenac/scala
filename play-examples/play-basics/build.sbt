name := "play-basics"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "Rally Health" at "https://dl.bintray.com/rallyhealth/maven"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "com.rallyhealth" %% "weepickle-v1" % "1.0.1",
  "com.softwaremill.macwire" % "macros_2.13" % "2.3.3",
  "com.softwaremill.common" %% "tagging" % "2.2.1",
  "com.typesafe.play" % "play-slick_2.13" % "5.0.0",
  "com.typesafe.play" % "play-slick-evolutions_2.13" % "5.0.0",
  "com.typesafe.slick" % "slick_2.13" % "3.3.2",
  "com.github.tminglei" % "slick-pg_2.13" % "0.18.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.10",

  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "1.0.0-alpha1" % Test,
  "com.dimafeng" %% "testcontainers-scala-postgresql" % "1.0.0-alpha1" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.mockito" %% "mockito-scala-scalatest" % "1.11.4" % Test



)

