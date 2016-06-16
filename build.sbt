name := "yfcc100m"

version := "1.0"

scalaVersion := "2.11.8"

val akkaV = "2.4.4"


libraryDependencies += "org.apache.commons" % "commons-compress" % "1.11"


libraryDependencies +=  "com.typesafe.akka" %% "akka-stream" % akkaV
    