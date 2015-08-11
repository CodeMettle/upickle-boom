import sbt.Keys._
import sbt._

lazy val main = project in file("main") settings Seq(
  name := "upickle-boom-main",
  scalaVersion := "2.11.7"
) dependsOn model

lazy val model = project in file("model") settings Seq(
  name := "upickle-boom-model",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq("com.lihaoyi" %% "upickle" % "0.3.5")
)
