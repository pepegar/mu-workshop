package com.adrianrafo.seed.server
package app

import cats.effect._
import com.adrianrafo.seed.server.common.models._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.auto._

abstract class ServerBoot {

  def runProgram(args: List[String]): IO[ExitCode] =
    for {
      config   <- Effect[IO].delay(pureconfig.loadConfigOrThrow[SeedServerConfig])
      logger   <- Slf4jLogger.fromName[IO](config.name)
      exitCode <- serverProgram(config)(logger)
    } yield exitCode

  def serverProgram(config: SeedServerConfig)(implicit L: Logger[IO]): IO[ExitCode]
}
