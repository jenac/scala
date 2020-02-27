name := "play-basics"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "Rally Health" at "https://dl.bintray.com/rallyhealth/maven"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "com.rallyhealth" %% "weepickle-v1" % "1.0.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test
)

