name := "Tempus"

version := "1.0"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
libraryDependencies += guice

scalaSource in Compile := baseDirectory.value / "app/"
resourceDirectory in Compile := baseDirectory.value / "app/resources"

scalaSource in Test := baseDirectory.value / "test/"
resourceDirectory in Test := baseDirectory.value / "test/resources"

unmanagedResourceDirectories in Compile += baseDirectory.value / "conf"
unmanagedResourceDirectories in Test += baseDirectory.value / "conf"

unmanagedResourceDirectories in Assets += baseDirectory.value / "public"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "com.mashape.unirest" % "unirest-java" % "1.3.0"
libraryDependencies += "commons-dbutils" % "commons-dbutils" % "1.7"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.15"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.5"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.8"

