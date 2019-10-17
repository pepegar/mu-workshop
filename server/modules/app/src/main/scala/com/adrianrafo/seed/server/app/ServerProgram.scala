package com.adrianrafo.seed.server
package app

import cats.effect._
import cats.implicits._
import com.adrianrafo.seed.server.common.models._
import com.adrianrafo.seed.server.process.PeopleServiceImpl
import com.adrianrafo.seed.server.protocol._
import higherkindness.mu.rpc.server._
import io.chrisdavenport.log4cats.Logger

class ServerProgram(implicit CE: ConcurrentEffect[IO]) extends ServerBoot {

  def serverProgram(config: SeedServerConfig)(implicit L: Logger[IO]): IO[ExitCode] = {

    val serverName = config.name

    implicit val PS: PeopleService[IO] = new PeopleServiceImpl

    for {
      peopleService <- PeopleService.bindService[IO]
      server        <- GrpcServer.default[IO](config.port, List(AddService(peopleService)))
      _             <- L.info(s"$serverName - Starting server at ${config.host}:${config.port}")
      exitCode      <- GrpcServer.server(server).as(ExitCode.Success)
    } yield exitCode

  }
}

