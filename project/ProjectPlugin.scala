import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
import mdoc.MdocPlugin.autoImport._
import sbt.Keys._
import sbt._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    //noinspection TypeAnnotation
    lazy val V = new {
      val log4cats       = "0.3.0"
      val logbackClassic = "1.2.3"
      val muRPC          = "0.18.4"
      val scopt          = "3.7.0"
      val silencer       = "1.4.3"
      val macroParadise  = "2.1.0"
      val pureconfig     = "0.10.2"
    }
  }

  import autoImport._

  private lazy val logSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "ch.qos.logback"    % "logback-classic" % V.logbackClassic,
      "io.chrisdavenport" %% "log4cats-core"  % V.log4cats,
      "io.chrisdavenport" %% "log4cats-slf4j" % V.log4cats
    )
  )

  lazy val serverProtocolSettings: Seq[Def.Setting[_]] = Seq(
    idlType := "avro",
    srcGenSerializationType := "AvroWithSchema",
    sourceGenerators in Compile += (srcGen in Compile).taskValue,
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-channel" % V.muRPC
    )
  )

  lazy val serverProcessSettings: Seq[Def.Setting[_]] = logSettings

  lazy val serverAppSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "io.higherkindness"     %% "mu-rpc-server" % V.muRPC,
      "com.github.pureconfig" %% "pureconfig"    % V.pureconfig
    )
  )

  lazy val clientRPCSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-netty" % V.muRPC
    )
  )

  lazy val clientAppSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "com.github.scopt"      %% "scopt"      % V.scopt,
      "com.github.pureconfig" %% "pureconfig" % V.pureconfig
    )
  )

  lazy val docsSettings: Seq[Def.Setting[_]] = Seq(
    mdocVariables := Map(
      "MU_VERSION" -> V.muRPC
    ),
    mdocOut := file("47deg-slides/slides/")
  )

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      addCompilerPlugin("org.scalamacros" % "paradise"        % V.macroParadise cross CrossVersion.full),
      addCompilerPlugin("com.github.ghik" % "silencer-plugin" % V.silencer cross CrossVersion.full),
      libraryDependencies += "com.github.ghik" % "silencer-lib" % V.silencer % Provided cross CrossVersion.full,
      organizationName := "AdrianRaFo",
      scalaVersion := "2.12.9",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xlint",
        "-Yno-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Xfuture",
        "-Ywarn-unused-import",
        "-P:silencer:pathFilters=target"
      )
    )
}
