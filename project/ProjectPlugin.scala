import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import sbt.Keys._
import sbt._

object ProjectPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {

    //noinspection TypeAnnotation
    lazy val V = new {
      val catsEffect     = "1.2.0"
      val log4cats       = "0.3.0"
      val logbackClassic = "1.2.3"
      val muRPC          = "0.18.0"
      val scopt          = "3.7.0"
      val pureconfig     = "0.10.2"
    }
  }

  import autoImport._

  private lazy val logSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "ch.qos.logback"    % "logback-classic" % V.logbackClassic,
      "io.chrisdavenport" %% "log4cats-core"  % V.log4cats,
      "io.chrisdavenport" %% "log4cats-slf4j" % V.log4cats
    ))

  lazy val configSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-effect" % V.catsEffect,
      "com.github.pureconfig" %% "pureconfig"  % V.pureconfig))

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
    libraryDependencies ++= Seq("io.higherkindness" %% "mu-rpc-server" % V.muRPC))

  lazy val clientRPCSettings: Seq[Def.Setting[_]] = logSettings ++ Seq(
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-netty" % V.muRPC,
      "io.higherkindness" %% "mu-rpc-fs2"   % V.muRPC
    )
  )

  lazy val clientAppSettings: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "com.github.scopt" %% "scopt" % V.scopt
    ))

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      organizationName := "AdrianRaFo",
      scalaVersion := "2.12.6",
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
        "-Ywarn-unused-import"
      ),
      scalafmtCheck := true,
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    )
}
