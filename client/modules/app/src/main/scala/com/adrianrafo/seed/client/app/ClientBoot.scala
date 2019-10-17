package com.adrianrafo.seed.client
package app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import com.adrianrafo.seed.client.process.PeopleServiceClient
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._

abstract class ClientBoot(implicit CE: ConcurrentEffect[IO], CS: ContextShift[IO]) {

  def peopleServiceClient(host: String, port: Int)(
      implicit L: Logger[IO]
  ): Resource[IO, PeopleServiceClient[IO]] =
    PeopleServiceClient.createClient(host, port)

  def runProgram(args: List[String]): Resource[IO, ExitCode] = {
    def setupConfig: IO[SeedClientConfig] =
      IO(ConfigSource.default.loadOrThrow[ClientConfig])
        .map(client => SeedClientConfig(client, ClientParams.loadParams(client.name, args)))

    for {
      config   <- Resource.liftF(setupConfig)
      logger   <- Resource.liftF(Slf4jLogger.fromName[IO](config.client.name))
      exitCode <- clientProgram(config)(logger)
    } yield exitCode
  }

  def clientProgram(config: SeedClientConfig)(implicit L: Logger[IO]): Resource[IO, ExitCode]
}
