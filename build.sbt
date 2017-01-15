name := "KafkaExample"

version := "1.0"

scalaVersion := "2.11.8"

val SparkVersion = "1.6.2-hdp2.5-1"

libraryDependencies += "org.apache.kafka" %% "kafka" % "0.10.0.1"

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "1.6.0" % Compile exclude("org.apache.cassandra", "cassandra-clientutil") excludeAll (ExclusionRule("io.netty"))

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-log4j12" % "1.7.21",
  "org.apache.spark" %% "spark-core" % "2.0.2",
  "org.apache.spark" %% "spark-sql" % "2.0.2"
)