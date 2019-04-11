package com.adrianrafo.seed.client.app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import fs2.Stream
import io.chrisdavenport.log4cats.Logger

class ClientProgram[F[_]: ConcurrentEffect: ContextShift] extends ClientBoot[F] {

  def clientProgram(config: SeedClientConfig)(implicit L: Logger[F]): Stream[F, ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.client.host, config.client.port)
      result       <- Stream.eval(peopleClient.getPerson(config.params.request))
    } yield result.fold(_ => ExitCode.Error, _ => ExitCode.Success)
  }
}

object ClientApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    new ClientProgram[IO]
      .runProgram(args)
      .compile
      .toList
      .map(_.headOption.getOrElse(ExitCode.Error))
}
