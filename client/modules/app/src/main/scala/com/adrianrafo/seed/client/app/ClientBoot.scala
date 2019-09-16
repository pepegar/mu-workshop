package com.adrianrafo.seed.client
package app

import cats.effect._
import cats.syntax.functor._
import com.adrianrafo.seed.client.common.models._
import com.adrianrafo.seed.client.process.PeopleServiceClient
import com.adrianrafo.seed.config.ConfigService
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.generic.auto._

abstract class ClientBoot[F[_]: ConcurrentEffect: ContextShift] {

  def peopleServiceClient(host: String, port: Int)(
      implicit L: Logger[F]
  ): Resource[F, PeopleServiceClient[F]] =
    PeopleServiceClient.createClient(host, port, sslEnabled = false)

  def runProgram(args: List[String]): Resource[F, ExitCode] = {
    def setupConfig: F[SeedClientConfig] =
      ConfigService[F]
        .serviceConfig[ClientConfig]
        .map(client => SeedClientConfig(client, ClientParams.loadParams(client.name, args)))

    for {
      config   <- Resource.liftF(setupConfig)
      logger   <- Resource.liftF(Slf4jLogger.fromName[F](config.client.name))
      exitCode <- clientProgram(config)(logger)
    } yield exitCode
  }

  def clientProgram(config: SeedClientConfig)(implicit L: Logger[F]): Resource[F, ExitCode]
}
