ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github"
ThisBuild / organizationName := "plavreshin"

val KantanVersion = "0.7.0"
val PekkoVersion = "1.0.2"
val PekkoHttpVersion = "1.0.0"
val PekkoJsonVersion = "1.0.0"
val CirceVersion = "0.14.6"

lazy val root = (project in file("."))
  .settings(
    name := "stream-csv-sources-pekko-task",
    scalaVersion := "2.13.12",
    scalacOptions ++=
      Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-explaintypes",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Wdead-code",
        "-Werror",
        "-Wnumeric-widen",
//        "-Wunused:implicits",
//        "-Wunused:imports",
//        "-Wunused:linted",
//        "-Wunused:locals",
//        "-Wunused:params",
//        "-Wunused:patvars",
//        "-Wunused:privates",
        "-Wvalue-discard",
        "-Xcheckinit",
        "-Xlint",
        "-Xsource:3"
      ),
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
      "org.apache.pekko" %% "pekko-stream-testkit" % PekkoVersion % Test,
      "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-http-testkit" % PekkoHttpVersion % Test,
      "org.apache.pekko" %% "pekko-actor" % PekkoVersion,
      "org.mdedetrich" %% "pekko-stream-json" % PekkoJsonVersion,
      "org.mdedetrich" %% "pekko-http-json" % PekkoJsonVersion,
      "org.mdedetrich" %% "pekko-http-circe" % PekkoJsonVersion,
      "org.mdedetrich" %% "pekko-stream-circe" % PekkoJsonVersion,
      "com.nrinaudo" %% "kantan.csv" % KantanVersion,
      "com.nrinaudo" %% "kantan.csv-generic" % KantanVersion,
      "com.nrinaudo" %% "kantan.csv-java8" % KantanVersion,
      "org.typelevel" %% "cats-core" % "2.10.0",
      "io.scalaland" %% "chimney" % "0.8.3",
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.scalatest" %% "scalatest" % "3.2.17" % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
