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

libraryDependencies ++= Seq(
	"com.typesafe.slick" %% "slick" % "2.1.0",
	"org.slf4j" % "slf4j-nop" % "1.6.4"
)
