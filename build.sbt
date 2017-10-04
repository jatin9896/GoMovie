name := "Go Movie"

version := "1.0"

scalaVersion := "2.12.1"

import Dependencies._
import ProjectSetting._

lazy val root = Project("ThePlatform", file("."))
  .settings(commands += Command.command("testUntilFailed") { state =>
    "test" :: "testUntilFailed" :: state
  })
  .settings(sbtAssemblySettings: _*)
  .aggregate(api, notifier, selector)

lazy val common = BaseProject("common").settings(
  libraryDependencies ++= compileDependencies(typesafeConfService.value ++ loggers.value)
    ++ testClassifierDependencies(Nil))

lazy val api = BaseProject("api").settings(
  libraryDependencies ++= compileDependencies(akkaHttp.value ++ akkaJson.value)
    ++ testClassifierDependencies(Nil)).dependsOn(common)

lazy val selector = BaseProject("selector").settings(
  libraryDependencies ++= compileDependencies(akkaHttp.value ++ akkaJson.value ++ cassandra.value ++ sqlJdbc.value)
    ++ testClassifierDependencies(Nil)).dependsOn(common)
  .settings(sbtAssemblySettings: _*)


lazy val notifier = BaseProject("notifier").settings(
  libraryDependencies ++= compileDependencies(akkaHttp.value ++ akkaJson.value ++ javaMailer.value)
    ++ testClassifierDependencies(Nil)).dependsOn(common)
