package com.adrianrafo.seed.client
package app

import cats.effect._
import cats.syntax.functor._
import com.adrianrafo.seed.client.common.models._
import com.adrianrafo.seed.config.ConfigService
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.auto._

abstract class ClientBoot[F[_]: Effect] {

  def program(
      args: List[String])(implicit TM: Timer[F], CE: ConcurrentEffect[F]): Stream[F, ExitCode] = {
    def setupConfig =
      ConfigService[F]
        .serviceConfig[ClientConfig]
        .map(client => SeedClientConfig(client, ClientParams.loadParams(client.name, args)))

    for {
      config   <- Stream.eval(setupConfig)
      logger   <- Stream.eval(Slf4jLogger.fromName[F](config.client.name))
      exitCode <- clientProgram(config)(logger, TM, CE)
    } yield exitCode
  }

  def clientProgram(config: SeedClientConfig)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, ExitCode]
}
