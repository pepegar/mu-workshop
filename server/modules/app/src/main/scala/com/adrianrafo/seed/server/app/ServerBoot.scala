package com.adrianrafo.seed.server
package app

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.adrianrafo.seed.server.common.models._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.auto._

abstract class ServerBoot[F[_]: ConcurrentEffect] {

  def runProgram(args: List[String]): F[ExitCode] =
    for {
      config   <- Effect[F].delay(pureconfig.loadConfigOrThrow[SeedServerConfig])
      logger   <- Slf4jLogger.fromName[F](config.name)
      exitCode <- serverProgram(config)(logger)
    } yield exitCode

  def serverProgram(config: SeedServerConfig)(implicit L: Logger[F]): F[ExitCode]
}
