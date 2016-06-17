name := "yfcc100m"

version := "1.0"

scalaVersion := "2.11.8"

val akkaV = "2.4.4"


libraryDependencies += "org.apache.commons" % "commons-compress" % "1.11"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.3.0"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "2.3.0"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-json4s" % "2.3.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaV
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"
    