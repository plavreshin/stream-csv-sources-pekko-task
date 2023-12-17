ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github"
ThisBuild / organizationName := "plavreshin"

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
        "-Wunused:implicits",
        "-Wunused:imports",
        "-Wunused:linted",
        "-Wunused:locals",
        "-Wunused:params",
        "-Wunused:patvars",
        "-Wunused:privates",
        "-Wvalue-discard",
        "-Xcheckinit",
        "-Xlint",
        "-Xsource:3"
      ),
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-stream" % "1.0.2",
      "org.apache.pekko" %% "pekko-actor" % "1.0.2",
      "org.mdedetrich" %% "pekko-stream-json" % "1.0.0",
      "org.mdedetrich" %% "pekko-http-json" % "1.0.0",
      "com.nrinaudo" %% "kantan.csv" % "0.7.0",
      "com.nrinaudo" %% "kantan.csv-generic" % "0.7.0",
      "com.nrinaudo" %% "kantan.csv-java8" % "0.7.0",
      "org.scalameta" %% "munit" % "0.7.29"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
