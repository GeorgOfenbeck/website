import Keys.{compile => sbtCompile}
import sbtcrossproject.{crossProject, CrossType}
import NativePackagerHelper._
import com.typesafe.sbt.packager.SettingsHelper._

lazy val Constants = _root_.website.Constants

lazy val commonSettings = Seq(
  scalaVersion := "2.12.10",
  organization := "com.ofenbeck"
)

lazy val server = (project in file("server")).settings(commonSettings).settings(
  version := "0.0.1-SNAPSHOT",
  name := "webserver",
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  resolvers ++= Seq(
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.jcenterRepo,
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "confluent" at "https://packages.confluent.io/maven/",
    Resolver.bintrayRepo("akka", "snapshots")
  ),
  fullResolvers := (Resolver.jcenterRepo +: fullResolvers.value),
  updateOptions := updateOptions.value.withLatestSnapshots(false),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "scalatags" % Constants.scalaTags,
    "com.vmunier" %% "scalajs-scripts" % "1.1.4",
    guice,
    specs2 % Test,
    ws
  ),
  makeDeploymentSettings(Universal, packageBin in Universal, "zip")
).enablePlugins(PlayScala, LauncherJarPlugin, UniversalPlugin, UniversalDeployPlugin)



lazy val client = (project in file("client")).settings(commonSettings).settings(
  version := "0.0.1-SNAPSHOT",
  name := "jsclient",
  scalaJSUseMainModuleInitializer := false,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
  ),
  fullResolvers := (Resolver.jcenterRepo +: fullResolvers.value),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "scalatags" % Constants.scalaTags,
  ),
  updateOptions := updateOptions.value.withLatestSnapshots(false)
).enablePlugins(ScalaJSPlugin, ScalaJSWeb)

// loads the lex_instagui_server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen { s: State => "project server" :: s }

// On Jenkins, the slave uses a special command to clone the project. We remove the "remotes/origin/" prefix.
