import sbt.Keys._
import sbt._

lazy val main = project in file("main") settings Seq(
  name := "upickle-boom-main",
  scalaVersion := "2.11.7"
) dependsOn modelJvm

lazy val model = (crossProject.crossType(CrossType.Pure) in file("model")).settings(Seq[Setting[_]](
  name := "upickle-boom-model",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq("com.lihaoyi" %%% "upickle" % "0.3.5")
): _*).jsConfigure(_ enablePlugins ScalaJSPlugin)

lazy val modelJvm = model.jvm
lazy val modelJs = model.js
