package com.adrianrafo.seed.server.app

import cats.effect.Effect
import com.adrianrafo.seed.common.SeedConfig
import com.adrianrafo.seed.config.ConfigService
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import fs2._
import monix.execution.Scheduler

abstract class ServerBoot[F[_]: Effect] extends StreamApp[F] {

  implicit val S: Scheduler = monix.execution.Scheduler.Implicits.global

  override def stream(args: List[String], requestShutdown: F[Unit]): Stream[F, StreamApp.ExitCode] =
    for {
      config   <- ConfigService[F].serviceConfig[SeedConfig]
      logger   <- Stream.eval(Slf4jLogger.fromName[F](config.name))
      exitCode <- serverStream(config)(logger)
    } yield exitCode

  def serverStream(config: SeedConfig)(implicit L: Logger[F]): Stream[F, StreamApp.ExitCode]
}
