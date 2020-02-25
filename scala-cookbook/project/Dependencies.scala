import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.0"
  lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.6.3"
  lazy val scalaParallelCollections = "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"
  lazy val mongoDriver = "org.mongodb.scala" %% "mongo-scala-driver" % "2.8.0"
}
