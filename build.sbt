name := "Tempus"

version := "1.0"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies += guice