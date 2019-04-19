name := "Tempus"

version := "1.0"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
libraryDependencies += guice

scalaSource in Compile := baseDirectory.value / "src/main/scala/"
resourceDirectory in Compile := baseDirectory.value / "src/main/resources"

scalaSource in Test := baseDirectory.value / "src/test/scala/"
resourceDirectory in Test := baseDirectory.value / "src/test/resources"

unmanagedResourceDirectories in Compile += baseDirectory.value / "conf"
unmanagedResourceDirectories in Test += baseDirectory.value / "conf"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "com.mashape.unirest" % "unirest-java" % "1.3.0"
libraryDependencies += "commons-dbutils" % "commons-dbutils" % "1.7"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.15"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.8"

