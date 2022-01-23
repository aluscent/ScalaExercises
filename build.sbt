name := "Rock-the-JVM_Advanced-Scala"

version := "0.1"
val akkaVersion = "2.6.16"
val akkaHttpVersion = "10.2.7"
scalaVersion := "2.13.0"

val dependency = List(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scala-lang" % "scala-reflect" % "3",

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,

  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,

  "org.scalatest" %% "scalatest" % "3.3.0-SNAP3",
  "org.slf4j" % "slf4j-simple" % "2.0.0-alpha5"
)

libraryDependencies ++= dependency