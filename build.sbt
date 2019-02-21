name := "Tempus"

version := "1.0"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies += guice

scalaSource in Compile := baseDirectory.value / "src/main/scala/"
resourceDirectory in Compile := baseDirectory.value / "src/main/resources"

scalaSource in Test := baseDirectory.value / "src/test/scala/"
resourceDirectory in Test := baseDirectory.value / "src/test/resources"
