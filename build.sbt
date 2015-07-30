name := """hello-scala"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1",
  "io.confluent" % "kafka-avro-serializer" % "1.0",
  "org.apache.avro" % "avro" % "1.7.7",
  "log4j" % "log4j" % "1.2.17",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0"
)

resolvers += "confluent" at "http://packages.confluent.io/maven/"
