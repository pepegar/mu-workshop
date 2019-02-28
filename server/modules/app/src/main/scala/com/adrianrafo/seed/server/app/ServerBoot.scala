package com.adrianrafo.seed.server
package app

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.adrianrafo.seed.config.ConfigService
import com.adrianrafo.seed.server.common.models._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.auto._

abstract class ServerBoot[F[_]: Effect] {

  def program(args: List[String])(implicit CE: ConcurrentEffect[F]): F[ExitCode] =
    for {
      config   <- ConfigService[F].serviceConfig[SeedServerConfig]
      logger   <- Slf4jLogger.fromName[F](config.server.name)
      exitCode <- serverProgram(config.server)(logger, CE)
    } yield exitCode

  def serverProgram(
      config: ServerConfig)(implicit L: Logger[F], CE: ConcurrentEffect[F]): F[ExitCode]
}
