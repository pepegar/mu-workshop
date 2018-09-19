package com.adrianrafo.seed.server
package app

import cats.effect.Effect
import com.adrianrafo.seed.server.common.models._
import com.adrianrafo.seed.config.ConfigService
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import fs2._
import monix.execution.Scheduler

abstract class ServerBoot[F[_]: Effect] extends StreamApp[F] {

  implicit val S: Scheduler = monix.execution.Scheduler.Implicits.global

  override def stream(args: List[String], requestShutdown: F[Unit]): Stream[F, StreamApp.ExitCode] =
    for {
      config   <- ConfigService[F].serviceConfig[SeedServerConfig]
      logger   <- Stream.eval(Slf4jLogger.fromName[F](config.server.name))
      exitCode <- serverStream(config.server)(logger)
    } yield exitCode

  def serverStream(config: ServerConfig)(implicit L: Logger[F]): Stream[F, StreamApp.ExitCode]
}
