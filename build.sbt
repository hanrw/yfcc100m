name := "yfcc100m"

version := "1.0"

scalaVersion := "2.12.5"

val akkaV = "2.4.20"

// major.minor are in sync with the elasticsearch releases
val elastic4sVersion = "6.2.4"
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-compress" % "1.11",

  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,

  // for the http client
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,

  // if you want to use reactive streams
  "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % elastic4sVersion,

  "com.typesafe.akka" %% "akka-stream" % akkaV,


  // testing
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)

