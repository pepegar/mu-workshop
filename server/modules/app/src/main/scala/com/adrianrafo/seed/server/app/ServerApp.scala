package com.adrianrafo.seed.server.app

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.adrianrafo.seed.common.SeedConfig
import com.adrianrafo.seed.protocol.PeopleService
import com.adrianrafo.seed.server.process.PeopleServiceHandler
import fs2.{Stream, StreamApp}
import io.chrisdavenport.log4cats.Logger
import freestyle.rpc.server._

class ServerProgram[F[_]: Effect] extends ServerBoot[F] {

  override def serverStream(config: SeedConfig)(
      implicit L: Logger[F]): Stream[F, StreamApp.ExitCode] = {

    val serverName = s"${config.name}"

    implicit val PS: PeopleService[F] = new PeopleServiceHandler[F]

    val grpcConfigs: List[GrpcConfig] = List(AddService(PeopleService.bindService[F]))

    Stream.eval(for {
      server   <- GrpcServer.default[F](config.port, grpcConfigs)
      _        <- L.info(s"$serverName - Starting server at ${config.host}:${config.port}")
      exitCode <- GrpcServer.server(server).as(StreamApp.ExitCode.Success)
    } yield exitCode)

  }
}

object ServerApp extends ServerProgram[IO]
