name := "scalac-chess-problem"

version := "0.1-SNAPSHOT"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.5" % "test"

javaOptions ++= List("-XX:MaxPermSize=4096", "-Xmx4g", "-Xms2g")

fork := true